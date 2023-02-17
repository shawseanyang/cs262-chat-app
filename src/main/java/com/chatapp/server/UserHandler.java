package com.chatapp.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.chatapp.protocol.Constants;
import com.chatapp.protocol.Marshaller;
import com.chatapp.protocol.Message;
import com.chatapp.protocol.MessageValidator;
import com.chatapp.protocol.Operation;

import com.chatapp.utility.ByteConverter;

public class UserHandler implements Runnable {

    private User user;
    private Socket socket;
    private Message message;
    private BufferedReader in;
    private DataOutputStream out;
    private boolean threadIsAlive;
    
    public UserHandler(Socket socket) {
        this.socket = socket;
        this.threadIsAlive = true;
    }
    
    @Override
    public void run() {
        // Set up the input and output streams
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            System.err.println("ERROR: Could not set up the input and output streams.");
            e.printStackTrace();
            return;
        }

        while(threadIsAlive) {
            // Read the message
            byte[] messageBytes;
            try {
                String messageString = in.readLine();
                if (messageString == null) {
                    System.out.println("Client disconnected.");
                    threadIsAlive = false;
                    return;
                }
                messageBytes = ByteConverter.stringToByteArray(messageString);
            } catch (IOException e) {
                System.err.println("ERROR: Could not read the message.");
                e.printStackTrace();
                continue;
            }

            // Unmarshall the message
            message = Marshaller.unmarshall(messageBytes);

            // Print the message
            if (user != null)
                System.out.println("Received message from " + ByteConverter.byteArrayToString(user.getUsername()) + ": " + message);
            else
                System.out.println("Received message: " + message);

            // Validate the message
            com.chatapp.protocol.Exception resultingException = validateMessage();
            if (resultingException != com.chatapp.protocol.Exception.NONE) {
                // Send an error message
                writeMessage(resultingException);
                continue;
            }
            
            // Handle the message
            Operation operation = message.getOperation();
            ArrayList<byte[]> args = message.getArguments();
            switch (operation) {
                case CREATE_ACCOUNT:
                    createAccountHandler(args.get(0));
                    break;
                case DELETE_ACCOUNT:
                    deleteAccountHandler(args.get(0));
                    break;
                case LOG_IN:
                    loginHandler(args.get(0));
                    break;
                case LOG_OUT:
                    logoutHandler();
                    break;
                case SEND_MESSAGE:
                    byte[] recipient = args.get(0);
                    byte[] message = args.get(1);
                    sendMessageHandler(recipient, message);
                    break;
                case LIST_ACCOUNTS:
                    listAccountsHandler(args.get(0));
                    break;
                case DISTRIBUTE_MESSAGE:
                    distributeMessageHandler();
                    break;
            }
        }
    }

    /* Sender-related functions */
    
    private void createAccountHandler(byte[] usernameBytes) {
        String username = ByteConverter.byteArrayToString(usernameBytes);

        // If username already exists, send an error message
        if (Server.clients.containsKey(username)) {
            writeMessage(com.chatapp.protocol.Exception.USER_ALREADY_EXISTS);
            return;
        }
        
        // Add the user with no assigned socket
        Server.clients.put(username, new User(usernameBytes));

        // Send a success message
        writeMessage(com.chatapp.protocol.Exception.NONE);
    }

    private void deleteAccountHandler(byte[] usernameBytes) {
        String username = ByteConverter.byteArrayToString(usernameBytes);

        // Check if user does not exist
        if (!Server.clients.containsKey(username)) {
            writeMessage(com.chatapp.protocol.Exception.USER_DOES_NOT_EXIST);
            return;
        }

        User user = Server.clients.get(username);

        // Delete the user
        user.clear();
        Server.clients.remove(username);

        // Send a success message
        writeMessage(com.chatapp.protocol.Exception.NONE);
    }

    private void loginHandler(byte[] usernameBytes) {
        String username = ByteConverter.byteArrayToString(usernameBytes);

        // If username does not exist, send an error message
        if (!Server.clients.containsKey(username)) {
            writeMessage(com.chatapp.protocol.Exception.USER_DOES_NOT_EXIST);
            return;
        }

        // Set the user variable
        this.user = Server.clients.get(username);

        // If client has already logged in, close the old socket
        Socket oldSocket = user.getSocket();
        if (oldSocket != null && oldSocket != socket) {
            try {
                oldSocket.close();
            } catch (IOException e) {
                System.err.println("ERROR: Could not close the socket.");
                e.printStackTrace();
            }
        }
        
        // Update the socket
        user.setSocket(socket);

        // Send a success message
        writeMessage(com.chatapp.protocol.Exception.NONE);
    }

    private void logoutHandler() {
        // Check if user is logged in
        if (!isLoggedIn()) {
            writeMessage(com.chatapp.protocol.Exception.NOT_LOGGED_IN);
            return;
        }

        // Close the socket
        try {
            Socket socket = user.getSocket();
            socket.close();
        } catch (IOException e) {
            System.err.println("ERROR: Could not close the socket.");
            e.printStackTrace();
        }

        // Remove the socket
        user.setSocket(null);

        // Set the user variable to null
        this.user = null;

        // Send a success message
        writeMessage(com.chatapp.protocol.Exception.NONE);
    }

    private void sendMessageHandler(byte[] recipient, byte[] message) {
        // Check if user is logged in
        if (!isLoggedIn()) {
            writeMessage(com.chatapp.protocol.Exception.NOT_LOGGED_IN);
            return;
        }

        User recipientUser = new User(recipient);

        // Add message to recipient's queue
        recipientUser.addMessage(user, message);

        // Send a success message
        writeMessage(com.chatapp.protocol.Exception.NONE);
    }

    private void listAccountsHandler(byte[] regex) {
        // Check if user is logged in
        if (!isLoggedIn()) {
            writeMessage(com.chatapp.protocol.Exception.NOT_LOGGED_IN);
            return;
        }

        // Compile the regex
        Pattern regexString = Pattern.compile(ByteConverter.byteArrayToString(regex));

        // Compare the query to the list of users
        ArrayList<byte[]> matchedUsers = new ArrayList<byte[]>();
        for(Map.Entry<String, User> e : Server.clients.entrySet()) {
            String otherString = e.getKey();
            User otherUser = e.getValue();

            Matcher matcher = regexString.matcher(otherString);

            // If the regex matches the username, add the username
            if (matcher.find()) {
                matchedUsers.add(otherUser.getUsername());
            }
        }

        // Send a success message
        writeMessage(com.chatapp.protocol.Exception.NONE, matchedUsers);
    }

    /* Recipient-related functions */

    private void distributeMessageHandler() {
        // Check if user is logged in
        if (!isLoggedIn()) {
            writeMessage(com.chatapp.protocol.Exception.NOT_LOGGED_IN);
            return;
        }

        // Get the messages
        while (user.hasMessages()) {
            ArrayList<byte[]> args = new ArrayList<byte[]>();
            
            User.QueuedMessage message = user.peekMessage();
            args.add(message.getSender().getUsername());
            args.add(message.getMessage());

            // Send the message
            writeMessage(com.chatapp.protocol.Exception.NONE, args);

            // Remove the message
            user.removeMessage();
        }
    }

    /* Utility functions */

    // Utility function for validating the message
    private com.chatapp.protocol.Exception validateMessage() {
        Operation operation = message.getOperation();
        ArrayList<byte[]> args = message.getArguments();

        // Check if the message is valid according to the wire protocol
        com.chatapp.protocol.Exception resultingException = MessageValidator.validateMessage(message);
        if (resultingException != com.chatapp.protocol.Exception.NONE) {
            return resultingException;
        }

        switch (operation) {
            case CREATE_ACCOUNT:
            case LOG_IN:
                // Check if the username is valid (not empty)
                if (args.get(0).length == 0) {
                    return com.chatapp.protocol.Exception.INVALID_USERNAME;
                }
                break;
            case SEND_MESSAGE:
                // Check if recipient exists
                if (!Server.clients.containsKey(ByteConverter.byteArrayToString(args.get(0)))) {
                    return com.chatapp.protocol.Exception.USER_DOES_NOT_EXIST;
                }
                break;
        }
        return com.chatapp.protocol.Exception.NONE;
    }

    // Utility functions for sending response messages to the client
    private void writeMessage(com.chatapp.protocol.Exception exception, ArrayList<byte[]> args) {
        Message responseMessage = new Message(Constants.CURRENT_VERSION, message.getOperation(), exception, args);
        try {
            out.writeUTF("MESSAGE SENT");
            // out.write(Marshaller.marshall(responseMessage));
            
            // Print sent message to console
            if (user != null)
                System.out.println("Sent response message to " + ByteConverter.byteArrayToString(user.getUsername()) + ": " + responseMessage);
            else
                System.out.println("Sent response message: " + responseMessage);
        } catch (IOException e) {
            if (exception == com.chatapp.protocol.Exception.NONE)
                System.err.println("ERROR: Could not send the success message.");
            else
                System.err.println("ERROR: Could not send the error message.");
            e.printStackTrace();

            // Log out the user upon failure to deliver a message
            logoutHandler();

            // Close the thread
            threadIsAlive = false;
        }
    }

    private void writeMessage(com.chatapp.protocol.Exception exception) {
        writeMessage(exception, new ArrayList<byte[]>());
    }

    // Utility function for determining if a user is logged in
    private boolean isLoggedIn() {
        return user != null && user.getSocket() != null;
    }

}
