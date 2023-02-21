package com.chatapp.protocol;

import java.util.HashMap;

/*
 * Validates whether messages have the correct number of arguments according to the protocol.
 */
public class MessageValidator {

  private static HashMap<Operation, Integer> ARG_COUNT = new HashMap<Operation, Integer>() {{
    put(Operation.CREATE_ACCOUNT, 1);
    put(Operation.DELETE_ACCOUNT, 1);
    put(Operation.LOG_IN, 1);
    put(Operation.LOG_OUT, 0);
    put(Operation.SEND_MESSAGE, 2);
    put(Operation.LIST_ACCOUNTS, 1);
    put(Operation.DISTRIBUTE_MESSAGE, 0);
  }};

  /*
   * Validates whether messages have the correct number of arguments according to the protocol.
   * @param message the message to validate
   * @return the protocol.Exception.NONE if the message is valid and an INVALID_ARGUMENT_COUNT otherwise
   */
  public static com.chatapp.protocol.Exception validateMessage(Message message) {
    Operation operation = message.getOperation();

    // Check if ARG_COUNT checks the operation
    if (!ARG_COUNT.containsKey(operation)) {
      throw new RuntimeException("Operation " + operation + " is not handled by validateMessage()");
    }

    // Check if correct number of arguments
    int argCount = ARG_COUNT.get(operation);
    if (message.getArguments().size() != argCount) {
      System.err.println("Expected " + argCount + " arguments, but got " + message.getArguments().size() + " arguments");
      return com.chatapp.protocol.Exception.INVALID_ARGUMENTS;
    }

    return com.chatapp.protocol.Exception.NONE;
  }
}