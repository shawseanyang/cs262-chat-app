package com.chatapp.protocol;

import java.util.ArrayList;
import java.util.HashMap;

public class MessageValidatorTest {
    public static void main(String[] args) {
        try {
            testMessageValidator();
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("MessageValidatorTest finished.");
        }
    }
  
    public static void testMessageValidator() throws java.lang.Exception {
        HashMap<Integer, ArrayList<byte[]>> numOfArgs = new HashMap<Integer, ArrayList<byte[]>>() {{
            put(0, new ArrayList<byte[]>());
            put(1, new ArrayList<byte[]>() {{add(new byte[0]);}});
            put(2, new ArrayList<byte[]>() {{add(new byte[0]);add(new byte[0]);}});
            put(3, new ArrayList<byte[]>() {{add(new byte[0]);add(new byte[0]);add(new byte[0]);}});
            put(4, new ArrayList<byte[]>() {{add(new byte[0]);add(new byte[0]);add(new byte[0]);add(new byte[0]);}});
            put(5, new ArrayList<byte[]>() {{add(new byte[0]);add(new byte[0]);add(new byte[0]);add(new byte[0]);add(new byte[0]);}});
        }};

        // Operation.CREATE_ACCOUNT
        Message faultyMessage = new Message(Constants.CURRENT_VERSION, Operation.CREATE_ACCOUNT, com.chatapp.protocol.Exception.NONE, numOfArgs.get(5));
        Message correctMessage = new Message(Constants.CURRENT_VERSION, Operation.CREATE_ACCOUNT, com.chatapp.protocol.Exception.NONE, numOfArgs.get(1));

        assert MessageValidator.validateMessage(faultyMessage) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
        assert MessageValidator.validateMessage(correctMessage) == com.chatapp.protocol.Exception.NONE;

        // Operation.DELETE_ACCOUNT
        faultyMessage = new Message(Constants.CURRENT_VERSION, Operation.DELETE_ACCOUNT, com.chatapp.protocol.Exception.NONE, numOfArgs.get(5));
        correctMessage = new Message(Constants.CURRENT_VERSION, Operation.DELETE_ACCOUNT, com.chatapp.protocol.Exception.NONE, numOfArgs.get(1));

        assert MessageValidator.validateMessage(faultyMessage) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
        assert MessageValidator.validateMessage(correctMessage) == com.chatapp.protocol.Exception.NONE;

        // TODO: Add all operations
    }
  }