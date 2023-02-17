package com.chatapp.client;

import java.io.IOException;
import java.util.ArrayList;

import com.chatapp.client.commands.CreateAccountCommand;
import com.chatapp.client.commands.DeleteAccountCommand;
import com.chatapp.client.commands.ListAccountsCommand;
import com.chatapp.client.commands.LogInCommand;
import com.chatapp.client.commands.LogOutCommand;
import com.chatapp.client.commands.SendMessageCommand;
import com.chatapp.client.exceptions.InvalidUsernameException;
import com.chatapp.client.exceptions.NotLoggedInException;
import com.chatapp.client.exceptions.UserAlreadyExistsException;
import com.chatapp.client.exceptions.UserDoesNotExistException;
import com.chatapp.protocol.Constants;
import com.chatapp.protocol.Marshaller;
import com.chatapp.protocol.Message;
import com.chatapp.protocol.Operation;
import com.chatapp.utility.ByteConverter;

// Helps the Client handle commands from the user because the Client is responsible for the UI logic. Each of the functions handles its command bu sending requests to the server, waiting for a response, then passing the response back to the caller.
public class ClientHandler {

  public static void createAccount(CreateAccountCommand command) throws UserAlreadyExistsException, InvalidUsernameException {
    // Convert to an argument arraylist
    ArrayList<byte[]> args = parseStringsToArguments(new String[]{command.getUsername()});

    // Send a message to the server
    sendMessage(Operation.CREATE_ACCOUNT, com.chatapp.protocol.Exception.NONE, args);

    // Wait for a response from the server
    Message response = readMessage();

    // Check the response for errors
    if (response.getException() == com.chatapp.protocol.Exception.USER_ALREADY_EXISTS)
      throw new UserAlreadyExistsException("The username " + command.getUsername() + " is already taken.");
    else if (response.getException() == com.chatapp.protocol.Exception.INVALID_USERNAME)
      throw new InvalidUsernameException("The username " + command.getUsername() + " is invalid.");
  }

  public static void deleteAccount(DeleteAccountCommand command) throws InvalidUsernameException, UserDoesNotExistException {
    // Convert to an argument arraylist
    ArrayList<byte[]> args = parseStringsToArguments(new String[]{command.getUsername()});

    // Create a message to send to the server
    sendMessage(Operation.DELETE_ACCOUNT, com.chatapp.protocol.Exception.NONE, args);

    // Wait for a response from the server
    Message response = readMessage();

    // Check the response for errors
    if (response.getException() == com.chatapp.protocol.Exception.INVALID_USERNAME)
      throw new InvalidUsernameException("The username " + command.getUsername() + " is invalid.");
    else if (response.getException() == com.chatapp.protocol.Exception.USER_DOES_NOT_EXIST)
      throw new UserDoesNotExistException("The username " + command.getUsername() + " does not exist.");
  }

  public static void listAccounts(ListAccountsCommand command) {
    // Convert to an argument arraylist
    ArrayList<byte[]> args = parseStringsToArguments(new String[]{command.getPattern()});

    // Create a message to send to the server
    sendMessage(Operation.LIST_ACCOUNTS, com.chatapp.protocol.Exception.NONE, args);
    
    // Wait for a response from the server
    Message response = readMessage();

    // Check the response for errors
    if (response.getException() != com.chatapp.protocol.Exception.NONE) {
      throw new RuntimeException("An unknown error occurred: " + response.getException());
    }
    // Print the list of accounts
    for (byte[] arg : response.getArguments()) {
      System.out.println(ByteConverter.byteArrayToString(arg));
    }
  }

  public static void logIn(LogInCommand command) throws UserDoesNotExistException {
    // Convert to an argument arraylist
    ArrayList<byte[]> args = parseStringsToArguments(new String[]{command.getUsername()});

    // Create a message to send to the server
    sendMessage(Operation.LOG_IN, com.chatapp.protocol.Exception.NONE, args);

    // Wait for a response from the server
    Message response = readMessage();

    // Check the response for errors
    if (response.getException() == com.chatapp.protocol.Exception.USER_DOES_NOT_EXIST)
      throw new UserDoesNotExistException("The username " + command.getUsername() + " does not exist.");
  }

  public static void logOut(LogOutCommand command) throws NotLoggedInException {
    // Create a message to send to the server
    sendMessage(Operation.LOG_OUT, com.chatapp.protocol.Exception.NONE);

    // Wait for a response from the server
    Message response = readMessage();

    // Check the response for errors
    if (response.getException() == com.chatapp.protocol.Exception.NOT_LOGGED_IN)
      throw new NotLoggedInException("You are not logged in.");
  }

  public static void sendMessage(SendMessageCommand command) throws UserDoesNotExistException, NotLoggedInException
  {
    // Convert to an argument arraylist
    ArrayList<byte[]> args = parseStringsToArguments(new String[]{command.getRecipient(), command.getMessage()});

    // Create a message to send to the server
    sendMessage(Operation.SEND_MESSAGE, com.chatapp.protocol.Exception.NONE, args);

    // Wait for a response from the server
    Message response = readMessage();

    // Check the response for errors
    if (response.getException() == com.chatapp.protocol.Exception.USER_DOES_NOT_EXIST)
      throw new UserDoesNotExistException("The username " + command.getRecipient() + " does not exist.");
    else if (response.getException() == com.chatapp.protocol.Exception.NOT_LOGGED_IN)
      throw new NotLoggedInException("You are not logged in.");
  }

  // Utility functions for communicating with the server
  private static void sendMessage(Operation operation, com.chatapp.protocol.Exception exception, ArrayList<byte[]> args) {
    Message message = new Message(Constants.CURRENT_VERSION, operation, exception, args);
    try {
        Client.socket_out.write(Marshaller.marshall(message));
    } catch (IOException e) {
        System.err.println("-> Could not send the message for " + operation + " to the server.");
        e.printStackTrace();
    }
  }

  private static void sendMessage(Operation operation, com.chatapp.protocol.Exception exception) {
      sendMessage(operation, exception, new ArrayList<byte[]>());
  }

  private static Message readMessage() {
    byte[] responseBytes = null;
    try {
      String responseString = Client.socket_in.readUTF();

      if (responseString == null) {
        throw new RuntimeException("The server shut down.");
      }

      responseBytes = ByteConverter.stringToByteArray(responseString);
      
    } catch (IOException e) {
      System.err.println("ERROR: Could not read the response from the server.");
      e.printStackTrace();
    }

    // Unmarshall the response
    Message response = Marshaller.unmarshall(responseBytes);

    return response;
  }

  // Utility functions for parsing arguments
  private static ArrayList<byte[]> parseStringsToArguments(String[] args) {
    ArrayList<byte[]> arguments = new ArrayList<byte[]>();
    for (String arg : args) {
      arguments.add(ByteConverter.stringToByteArray(arg));
    }
    return arguments;
  }
}
