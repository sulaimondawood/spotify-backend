package com.dawood.spotify.Exceptions.user;

public class UserAlreadyExists extends RuntimeException {

  public UserAlreadyExists() {
    super("User already exists");
  }

  public UserAlreadyExists(String message) {
    super(message);
  }

}
