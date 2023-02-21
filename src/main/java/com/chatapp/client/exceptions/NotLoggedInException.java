package com.chatapp.client.exceptions;

/*
 * This exception is thrown when a user tries to perform an action that requires
 * them to be logged in, but they are not.
 */
public class NotLoggedInException extends Exception {
  public NotLoggedInException(String message) {
    super(message);
  }
}
