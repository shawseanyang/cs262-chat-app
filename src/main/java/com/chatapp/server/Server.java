package com.chatapp.server;

import com.chatapp.protocol.Constants;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;

public class Server {

    public static void runTHIS() {
        try {
            ServerSocket ss = new ServerSocket(Constants.PORT);
            Socket s = ss.accept();// establishes connection
            DataInputStream dis = new DataInputStream(s.getInputStream());
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            while (true) {
              String str = (String) dis.readUTF();
              out.writeUTF("hello from server");
              System.out.println("message= " + str);
            }
          } catch (Exception e) {
            System.out.println(e);
          }
    }

    // Inactive users should have a socket value of null
    // <Username, User>
    static HashMap<String, User> clients = new HashMap<String, User>();
    public static void main(String[] args) throws IOException {
        runTHIS();

        System.out.println("Server is running...");

        // Create a new server socket
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(Constants.PORT);
        } catch (IOException e) {
            System.err.println("FATAL: Could not create a new server socket.");
            e.printStackTrace();
            ss.close();
            return;
        }

        while (true) {
            // Accept a new connection
            Socket s;
            try {
                s = ss.accept();
            } catch (IOException e) {
                System.err.println("ERROR: Could not accept a new connection.");
                e.printStackTrace();
                continue;
            }

            // Create a new thread to handle the client
            new Thread(new UserHandler(s)).start();
        }
    }
}