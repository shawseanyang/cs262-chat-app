package com.chatapp.client.commands;

/*
 * This command is used to log in to an existing account.
 */
public class LogInCommand implements Command {
  private String username;

  public LogInCommand(String username) throws IllegalArgumentException {
    if (username == null || username.isEmpty()) {
      throw new IllegalArgumentException("Username cannot be null or empty");
    }
    this.username = username;
  }

  public String getUsername() {
    return username;
  }
}
