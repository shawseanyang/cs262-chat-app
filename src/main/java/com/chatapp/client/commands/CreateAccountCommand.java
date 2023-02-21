package com.chatapp.client.commands;

/*
 * This command is used to create a new account.
 */
public class CreateAccountCommand implements Command {
  private String username;

  public CreateAccountCommand(String username) throws IllegalArgumentException {
    if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("Username cannot be null or empty");
    }
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
