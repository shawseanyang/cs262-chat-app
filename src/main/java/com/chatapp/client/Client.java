package com.chatapp.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

import com.chatapp.client.commands.Command;
import com.chatapp.client.commands.ConnectCommand;
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

// Entry point of the client application. Listens for user commands from the console and executes them. This class is responsible for UI logic. It is responsible for creating a channel to the server, and then passing the channel to the handlers.

public class Client {
  static Scanner command_in = new Scanner(System.in);
  static Socket socket;
  static DataInputStream socket_in;
  static OutputStream socket_out;

  public static void runTEMP() {
    Socket s = null;
		DataInputStream din = null;
		DataOutputStream dout = null;
		try {
			s = new Socket("localhost", Constants.PORT);
			din = new DataInputStream(s.getInputStream());
			dout=new DataOutputStream(s.getOutputStream());
			int i = 1;
			while(i++ < 100) {
				dout.writeUTF("Hello Server");
				dout.flush();
				String str = din.readUTF();
				System.out.println(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			dout.close();
//			s.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }

  public static void main(String[] args) {
    runTEMP();
    // listen for new user commands from the console
    while(true) {
      // read the next command from the console
      Command command;
      try {
        command = CommandParser.parse(command_in.nextLine());
      } catch (IllegalArgumentException e) {
        System.out.println("-> Error: " + e.getMessage());
        continue;
      }

      // when its a connect command, create a new channel to the server
      if (command instanceof ConnectCommand) {
        ConnectCommand cast = (ConnectCommand) command;
        // create a new socket to the server
        try {
          socket = new Socket(cast.getHost(), cast.getPort());

          // create input and output streams to the server
          try {
            socket_in = new DataInputStream(socket.getInputStream());
            // server_in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socket_out = socket.getOutputStream();
          } catch (IOException e) {
            System.out.println("-> Error: Could not create input and output streams to the server.");
            e.printStackTrace();
          }
        } catch (IOException e) {
          System.out.println("-> Error: Could not connect to the specified server.");
          e.printStackTrace();
        }
        continue;
      }
      
      // if there is no socket, then the user must connect first
      if (socket == null) {
        System.out.println("-> Error: You must connect to a server first.");
        continue;
      }

      if (command instanceof CreateAccountCommand) {
        CreateAccountCommand cast = (CreateAccountCommand) command;
        try {
          // -- TESTING --
          ClientHandler.createAccount(cast);
          System.out.println("-> Account created successfully.");
        } catch (UserAlreadyExistsException e) {
          System.out.println("-> Error: User already exists.");
          e.printStackTrace();
        } catch (InvalidUsernameException e) {
          System.out.println("-> Error: Invalid username.");
          e.printStackTrace();
        }
      }
      else if (command instanceof DeleteAccountCommand) {
        DeleteAccountCommand cast = (DeleteAccountCommand) command;
        try {
          ClientHandler.deleteAccount(cast);
        } catch (InvalidUsernameException e) {
          System.out.println("-> Error: Invalid username.");
          e.printStackTrace();
        } catch (UserDoesNotExistException e) {
          System.out.println("-> Error: User does not exist.");
          e.printStackTrace();
        }
      }
      else if (command instanceof ListAccountsCommand) {
        ListAccountsCommand cast = (ListAccountsCommand) command;
        ClientHandler.listAccounts(cast);
      }
      else if (command instanceof LogInCommand) {
        LogInCommand cast = (LogInCommand) command;
        try {
          ClientHandler.logIn(cast);
        } catch (UserDoesNotExistException e) {
          System.out.println("-> Error: User does not exist.");
          e.printStackTrace();
        }
      }
      else if (command instanceof LogOutCommand) {
        LogOutCommand cast = (LogOutCommand) command;
        try {
          ClientHandler.logOut(cast);
        } catch (NotLoggedInException e) {
          System.out.println("-> Error: You are not logged in.");
          e.printStackTrace();
        }
      }
      else if (command instanceof SendMessageCommand) {
        SendMessageCommand cast = (SendMessageCommand) command;
        try {
          ClientHandler.sendMessage(cast);
        } catch (UserDoesNotExistException e) {
          System.out.println("-> Error: User does not exist.");
          e.printStackTrace();
        } catch (NotLoggedInException e) {
          System.out.println("-> Error: You are not logged in.");
          e.printStackTrace();
        }
      }
    }
  }
}
