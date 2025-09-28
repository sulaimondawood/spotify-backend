package com.dawood.spotify.utils;

public class SongUtils {

  public static String formatTrackDuration(Double duration) {

    if (duration == null)
      return "0:00";

    int durationInMins = (int) (duration / 60);

    int durationSecondsRemainder = (int) (duration % 60);

    return String.format("%d:%02d", durationInMins, durationSecondsRemainder);

  }

}
