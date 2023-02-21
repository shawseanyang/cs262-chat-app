package com.chatapp.client.exceptions;

/*
 * This exception is thrown when a user tries to create an account with a username that already exists.
 */
public class UserAlreadyExistsException extends Exception {
  public UserAlreadyExistsException(String message) {
    super(message);
  }
}
