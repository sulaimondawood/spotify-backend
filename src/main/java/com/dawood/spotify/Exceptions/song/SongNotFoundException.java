package com.dawood.spotify.exceptions.song;

public class SongNotFoundException extends RuntimeException {

  public SongNotFoundException() {
    super("Song not found!");
  }

  public SongNotFoundException(String message) {
    super(message);
  }

}
