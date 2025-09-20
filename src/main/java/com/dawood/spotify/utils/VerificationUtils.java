package com.dawood.spotify.utils;

import java.security.SecureRandom;

public class VerificationUtils {

  public static int generateSixDigitsCode() {

    SecureRandom random = new SecureRandom();

    int code = random.nextInt(900000) + 100000;

    return code;
  }
}