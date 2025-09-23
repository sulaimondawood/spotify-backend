package com.dawood.spotify.exceptions.verification;

public class InvalidCodeException extends RuntimeException {

  public InvalidCodeException(String message) {
    super(message);
  }
}
