package protocol;

import java.util.HashMap;
import utility.ByteConverter;

// Validates whether messages are following the wire protocol in the sense that they have the correct number of arguments. Does not check if the arguments make sense or not (that's business logic for the serverside).

public class MessageValidator {

  private static HashMap<Operation, Integer> ARG_COUNT = new HashMap<Operation, Integer>() {{
    put(Operation.CREATE_ACCOUNT, 1);
    put(Operation.DELETE_ACCOUNT, 0);
    put(Operation.LOG_IN, 1);
    put(Operation.LOG_OUT, 0);
    put(Operation.SEND_MESSAGE, 3);
    put(Operation.LIST_ACCOUNTS, 1);
    put(Operation.DISTRIBUTE_MESSAGE, 0);
    put(Operation.DISTRIBUTE_MESSAGE_RESPONSE, 1);
  }};

  // returns protocol.Exception.NONE if the message is valid and a INVALID_ARGUMENT_COUNT otherwise
  public static protocol.Exception validateMessage(Message message) {
    Operation operation = message.getOperation();

    // Check if ARG_COUNT checks the operation
    if (!ARG_COUNT.containsKey(operation)) {
      throw new RuntimeException("Operation " + operation + " is not handled by validateMessage()");
    }

    // Check if correct number of arguments
    int argCount = ARG_COUNT.get(operation);
    if (message.getArguments().size() != argCount) {
      return protocol.Exception.INVALID_ARGUMENTS;
    }

    return protocol.Exception.NONE;
  }
}