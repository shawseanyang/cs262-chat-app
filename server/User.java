package server;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.UUID;

public class User {

    static class QueuedMessage {
        private User sender;
        private byte[] message;
        private UUID id;

        public QueuedMessage(User sender, byte[] message, UUID id) {
            this.sender = sender;
            this.message = message;
            this.id = id;
        }

        public User getSender() {
            return sender;
        }
    
        public byte[] getMessage() {
            return message;
        }
    
        public UUID getMessageId() {
            return id;
        }
    }

    private byte[] username;
    private Queue<QueuedMessage> messages;
    private HashSet<UUID> undeliveredMessages;

    public User(byte[] username) {
        this.username = username;
        this.messages = new LinkedList<QueuedMessage>();
        this.undeliveredMessages = new HashSet<UUID>();
    }

    public byte[] getUsername() {
        return username;
    }

    public void addMessage(User sender, byte[] message, UUID messageID) {
        // Don't add duplicate messages
        if (undeliveredMessages.contains(messageID))
            return;
        
        messages.add(new QueuedMessage(sender, message, messageID));
        undeliveredMessages.add(messageID);
    }
    
    public boolean hasMessages() {
        return !messages.isEmpty();
    }

    public QueuedMessage peekMessage() {
        return messages.peek();
    }

    public QueuedMessage removeMessage() {
        QueuedMessage message = messages.poll();
        undeliveredMessages.remove(message.id);
        return message;
    }

    public void clear() {
        messages.clear();
        undeliveredMessages.clear();
        username = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        return Arrays.equals(username, user.username);
    }
}