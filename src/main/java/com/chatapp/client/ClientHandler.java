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

// define new exceptions below UserAlreadyExists and UserDoesNotExist


public class ClientHandler {
  public static void createAccount(CreateAccountCommand command) throws UserAlreadyExistsException, InvalidUsernameException {
    // Convert the username to an argument
    ArrayList<byte[]> args = new ArrayList<byte[]>();
    args.add(ByteConverter.stringToByteArray(command.getUsername()));

    // Create a message to send to the server
    sendMessage(Operation.CREATE_ACCOUNT, com.chatapp.protocol.Exception.NONE, args);

    // Wait for a response from the server
    byte[] responseBytes = null;
    try {
      responseBytes = Client.server_in.readAllBytes();
    } catch (IOException e) {
      System.err.println("ERROR: Could not read the response from the server.");
      e.printStackTrace();
    }

    // Unmarshall the response
    Message response = Marshaller.unmarshall(responseBytes);

    // Check the response for errors
    if (response.getException() == com.chatapp.protocol.Exception.USER_ALREADY_EXISTS)
      throw new UserAlreadyExistsException("The username " + command.getUsername() + " is already taken.");
    else if (response.getException() == com.chatapp.protocol.Exception.INVALID_USERNAME)
      throw new InvalidUsernameException("The username " + command.getUsername() + " is invalid.");
  }

  public static void deleteAccount(DeleteAccountCommand command) throws InvalidUsernameException {
    // Convert the username to an argument
    ArrayList<byte[]> args = new ArrayList<byte[]>();
    args.add(ByteConverter.stringToByteArray(command.getUsername()));

    // Create a message to send to the server
    sendMessage(Operation.CREATE_ACCOUNT, com.chatapp.protocol.Exception.NONE, args);

    // Wait for a response from the server
    byte[] responseBytes = null;
    try {
      responseBytes = Client.server_in.readAllBytes();
    } catch (IOException e) {
      System.err.println("ERROR: Could not read the response from the server.");
      e.printStackTrace();
    }

    // Unmarshall the response
    Message response = Marshaller.unmarshall(responseBytes);

    // Check the response for errors
    if (response.getException() == com.chatapp.protocol.Exception.INVALID_USERNAME)
      throw new InvalidUsernameException("The username " + command.getUsername() + " is invalid.");
  }

  public static void listAccounts(ListAccountsCommand command) {

  }

  public static void logIn(LogInCommand command) throws UserDoesNotExistException {

  }

  public static void logOut(LogOutCommand command) throws NotLoggedInException {

  }

  public static void sendMessage(SendMessageCommand command) throws UserDoesNotExistException, NotLoggedInException
  {

  }

  // Utility functions for sending response messages to the client
  private static void sendMessage(Operation operation, com.chatapp.protocol.Exception exception, ArrayList<byte[]> args) {
    Message message = new Message(Constants.CURRENT_VERSION, operation, exception, args);
    try {
        Client.server_out.write(Marshaller.marshall(message));
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

  private static void sendMessage(Operation operation, com.chatapp.protocol.Exception exception) {
      sendMessage(operation, exception, new ArrayList<byte[]>());
  }
}
