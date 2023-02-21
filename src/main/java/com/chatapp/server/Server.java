package com.chatapp.server;

import com.chatapp.protocol.Constants;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ConcurrentHashMap;

/*
 * This class is used to represent the server.
 */
public class Server {

    /*
     * This map is used to map all the usernames to their respective User objects.
     * Inactive users should have a socket value of null.
     */
    static ConcurrentHashMap<String, User> clients = new ConcurrentHashMap<String, User>();
    public static void main(String[] args) throws IOException {
        System.out.println("[Server] Server is running...");

        // Create a new server socket
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(Constants.PORT);
        } catch (IOException e) {
            System.err.println("[Server] FATAL: Could not create a new server socket.");
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
                System.err.println("[Server] ERROR: Could not accept a new connection.");
                e.printStackTrace();
                continue;
            }
            System.out.println("[Server] New client connected from port " + s.getPort());

            // Create a new thread to handle the client
            new Thread(new UserHandler(s)).start();
        }
    }
}