package com.dawood.spotify.Exceptions.user;

public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException() {
    super("User not found");
  }

  public UserNotFoundException(String message) {
    super(message);
  }

}
