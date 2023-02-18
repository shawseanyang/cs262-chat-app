package com.chatapp.protocol;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Test;

import com.chatapp.utility.ByteConverter;

public class MessageValidatorTest {
    static HashMap<Integer, ArrayList<byte[]>> numOfArgs = new HashMap<Integer, ArrayList<byte[]>>() {{
        put(0, new ArrayList<byte[]>());
        put(1, new ArrayList<byte[]>() {{add(ByteConverter.stringToByteArray("username"));}});
        put(2, new ArrayList<byte[]>() {{add(ByteConverter.stringToByteArray("username"));add(ByteConverter.stringToByteArray("message"));}});
        put(3, new ArrayList<byte[]>() {{add(new byte[0]);add(new byte[0]);add(new byte[0]);}});
    }};

    @Test
    public void createAccount_with_InvalidArguments() {
        Message tooFewArguments = new Message(Constants.CURRENT_VERSION, Operation.CREATE_ACCOUNT, com.chatapp.protocol.Exception.NONE, numOfArgs.get(0));
        Message tooManyArguments = new Message(Constants.CURRENT_VERSION, Operation.CREATE_ACCOUNT, com.chatapp.protocol.Exception.NONE, numOfArgs.get(2));

        assert MessageValidator.validateMessage(tooFewArguments) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
        assert MessageValidator.validateMessage(tooManyArguments) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
    }

    @Test
    public void createAccount_with_ValidArguments() {
        Message correctMessage = new Message(Constants.CURRENT_VERSION, Operation.CREATE_ACCOUNT, com.chatapp.protocol.Exception.NONE, numOfArgs.get(1));
        assert MessageValidator.validateMessage(correctMessage) == com.chatapp.protocol.Exception.NONE;
    }

    @Test
    public void deleteAccount_with_InvalidArguments() {
        Message tooFewArguments = new Message(Constants.CURRENT_VERSION, Operation.DELETE_ACCOUNT, com.chatapp.protocol.Exception.NONE, numOfArgs.get(0));
        Message tooManyArguments = new Message(Constants.CURRENT_VERSION, Operation.DELETE_ACCOUNT, com.chatapp.protocol.Exception.NONE, numOfArgs.get(2));

        assert MessageValidator.validateMessage(tooFewArguments) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
        assert MessageValidator.validateMessage(tooManyArguments) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
    }

    @Test
    public void deleteAccount_with_ValidArguments() {
        Message correctMessage = new Message(Constants.CURRENT_VERSION, Operation.DELETE_ACCOUNT, com.chatapp.protocol.Exception.NONE, numOfArgs.get(1));
        assert MessageValidator.validateMessage(correctMessage) == com.chatapp.protocol.Exception.NONE;
    }

    @Test
    public void login_with_InvalidArguments() {
        Message tooFewArguments = new Message(Constants.CURRENT_VERSION, Operation.LOG_IN, com.chatapp.protocol.Exception.NONE, numOfArgs.get(0));
        Message tooManyArguments = new Message(Constants.CURRENT_VERSION, Operation.LOG_IN, com.chatapp.protocol.Exception.NONE, numOfArgs.get(2));

        assert MessageValidator.validateMessage(tooFewArguments) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
        assert MessageValidator.validateMessage(tooManyArguments) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
    }

    @Test
    public void login_with_ValidArguments() {
        Message correctMessage = new Message(Constants.CURRENT_VERSION, Operation.LOG_IN, com.chatapp.protocol.Exception.NONE, numOfArgs.get(1));
        assert MessageValidator.validateMessage(correctMessage) == com.chatapp.protocol.Exception.NONE;
    }

    @Test
    public void logout_with_InvalidArguments() {
        Message tooManyArguments = new Message(Constants.CURRENT_VERSION, Operation.LOG_OUT, com.chatapp.protocol.Exception.NONE, numOfArgs.get(1));
        assert MessageValidator.validateMessage(tooManyArguments) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
    }

    @Test
    public void logout_with_ValidArguments() {
        Message correctMessage = new Message(Constants.CURRENT_VERSION, Operation.LOG_OUT, com.chatapp.protocol.Exception.NONE, numOfArgs.get(0));
        assert MessageValidator.validateMessage(correctMessage) == com.chatapp.protocol.Exception.NONE;
    }

    @Test
    public void sendMessage_with_InvalidArguments() {
        Message tooFewArguments = new Message(Constants.CURRENT_VERSION, Operation.SEND_MESSAGE, com.chatapp.protocol.Exception.NONE, numOfArgs.get(0));
        Message tooManyArguments = new Message(Constants.CURRENT_VERSION, Operation.SEND_MESSAGE, com.chatapp.protocol.Exception.NONE, numOfArgs.get(3));

        assert MessageValidator.validateMessage(tooFewArguments) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
        assert MessageValidator.validateMessage(tooManyArguments) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
    }

    @Test
    public void sendMessage_with_ValidArguments() {
        Message correctMessage = new Message(Constants.CURRENT_VERSION, Operation.SEND_MESSAGE, com.chatapp.protocol.Exception.NONE, numOfArgs.get(2));
        assert MessageValidator.validateMessage(correctMessage) == com.chatapp.protocol.Exception.NONE;
    }

    @Test
    public void listAccounts_with_InvalidArguments() {
        Message tooFewArguments = new Message(Constants.CURRENT_VERSION, Operation.LIST_ACCOUNTS, com.chatapp.protocol.Exception.NONE, numOfArgs.get(0));
        Message tooManyArguments = new Message(Constants.CURRENT_VERSION, Operation.LIST_ACCOUNTS, com.chatapp.protocol.Exception.NONE, numOfArgs.get(2));

        assert MessageValidator.validateMessage(tooFewArguments) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
        assert MessageValidator.validateMessage(tooManyArguments) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
    }

    @Test
    public void listAccounts_with_ValidArguments() {
        Message correctMessage = new Message(Constants.CURRENT_VERSION, Operation.LIST_ACCOUNTS, com.chatapp.protocol.Exception.NONE, numOfArgs.get(1));
        assert MessageValidator.validateMessage(correctMessage) == com.chatapp.protocol.Exception.NONE;
    }

    @Test
    public void distributeMessage_with_InvalidArguments() {
        Message tooManyArguments = new Message(Constants.CURRENT_VERSION, Operation.DISTRIBUTE_MESSAGE, com.chatapp.protocol.Exception.NONE, numOfArgs.get(1));
        assert MessageValidator.validateMessage(tooManyArguments) == com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
    }

    @Test
    public void distributeMessage_with_ValidArguments() {
        Message correctMessage = new Message(Constants.CURRENT_VERSION, Operation.DISTRIBUTE_MESSAGE, com.chatapp.protocol.Exception.NONE, numOfArgs.get(0));
        assert MessageValidator.validateMessage(correctMessage) == com.chatapp.protocol.Exception.NONE;
    }
  }