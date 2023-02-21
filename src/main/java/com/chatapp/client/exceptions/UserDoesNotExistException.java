package com.chatapp.client.exceptions;

/*
 * This exception is thrown when a user does not exist.
 */
public class UserDoesNotExistException extends Exception {
  public UserDoesNotExistException(String message) {
    super(message);
  }
}
