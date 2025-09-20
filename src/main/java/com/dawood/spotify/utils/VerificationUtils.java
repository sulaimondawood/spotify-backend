package com.dawood.spotify.utils;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class VerificationUtils {

  public int generateSixDigitsCode() {

    SecureRandom random = new SecureRandom();

    int code = random.nextInt(900000) + 100000;

    return code;
  }
}