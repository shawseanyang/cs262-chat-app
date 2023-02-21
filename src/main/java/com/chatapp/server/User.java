package com.chatapp.server;

import java.net.Socket;
import java.util.LinkedList;
import java.util.Queue;

/*
 * This class is used to represent a user.
 */
public class User {

    /*
     * This class is used to represent a queued message.
     */
    static class QueuedMessage {
        private User sender;
        private byte[] message;

        public QueuedMessage(User sender, byte[] message) {
            this.sender = sender;
            this.message = message;
        }

        public User getSender() {
            return sender;
        }
    
        public byte[] getMessage() {
            return message;
        }
    }

    private byte[] username;
    private Socket socket;

    private Queue<QueuedMessage> messages;

    public User(byte[] username) {
        this.username = username;
        this.socket = null;
        this.messages = new LinkedList<QueuedMessage>();
    }

    public byte[] getUsername() {
        return username;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public void addMessage(User sender, byte[] message) {
        messages.add(new QueuedMessage(sender, message));
    }
    
    public boolean hasMessages() {
        return !messages.isEmpty();
    }

    public QueuedMessage peekMessage() {
        return messages.peek();
    }

    public QueuedMessage removeMessage() {
        QueuedMessage message = messages.poll();
        return message;
    }

    /*
     * This method is used to clear the user's messages and username.
     * This method should be called when the user is deleted.
     */
    public void clear() {
        messages.clear();
        username = null;
    }
}