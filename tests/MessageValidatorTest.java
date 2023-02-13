package tests;

import java.util.ArrayList;
import java.util.HashMap;

import protocol.Constants;
import protocol.Message;
import protocol.MessageValidator;
import protocol.Operation;

public class MessageValidatorTest {
    public static void main(String[] args) {
        try {
            testMessageValidator();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("MessageValidatorTest finished.");
        }
    }
  
    public static void testMessageValidator() throws Exception {
        HashMap<Integer, ArrayList<byte[]>> numOfArgs = new HashMap<Integer, ArrayList<byte[]>>() {{
            put(0, new ArrayList<byte[]>());
            put(1, new ArrayList<byte[]>() {{add(new byte[0]);}});
            put(2, new ArrayList<byte[]>() {{add(new byte[0]);add(new byte[0]);}});
            put(3, new ArrayList<byte[]>() {{add(new byte[0]);add(new byte[0]);add(new byte[0]);}});
            put(4, new ArrayList<byte[]>() {{add(new byte[0]);add(new byte[0]);add(new byte[0]);add(new byte[0]);}});
            put(5, new ArrayList<byte[]>() {{add(new byte[0]);add(new byte[0]);add(new byte[0]);add(new byte[0]);add(new byte[0]);}});
        }};

        // Operation.CREATE_ACCOUNT
        Message faultyMessage = new Message(Constants.CURRENT_VERSION, Operation.CREATE_ACCOUNT, protocol.Exception.NONE, numOfArgs.get(5));
        Message correctMessage = new Message(Constants.CURRENT_VERSION, Operation.CREATE_ACCOUNT, protocol.Exception.NONE, numOfArgs.get(1));

        assert MessageValidator.validateMessage(faultyMessage) == protocol.Exception.INVALID_ARGUMENTS;
        assert MessageValidator.validateMessage(correctMessage) == protocol.Exception.NONE;

        // Operation.DELETE_ACCOUNT
        faultyMessage = new Message(Constants.CURRENT_VERSION, Operation.DELETE_ACCOUNT, protocol.Exception.NONE, numOfArgs.get(5));
        correctMessage = new Message(Constants.CURRENT_VERSION, Operation.DELETE_ACCOUNT, protocol.Exception.NONE, numOfArgs.get(1));

        assert MessageValidator.validateMessage(faultyMessage) == protocol.Exception.INVALID_ARGUMENTS;
        assert MessageValidator.validateMessage(correctMessage) == protocol.Exception.NONE;

        // TODO: Add all operations
    }
  }