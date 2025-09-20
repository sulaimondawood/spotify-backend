package com.dawood.spotify.Exceptions;

public class InvalidCodeException extends RuntimeException {

  public InvalidCodeException(String message) {
    super(message);
  }
}
