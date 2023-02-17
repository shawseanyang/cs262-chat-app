package com.chatapp.client.commands;

import com.chatapp.client.exceptions.UserDoesNotExistException;

public class DeleteAccountCommand implements Command {
  private String username;

  public DeleteAccountCommand(String username) throws IllegalArgumentException {
    if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("Username cannot be null or empty");
    }

    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
