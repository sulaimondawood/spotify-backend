package com.dawood.spotify.utils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.annotation.PostConstruct;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

  @Value("${app.jwt.secret}")
  private String secret;

  private Algorithm algorithm;

  @PostConstruct
  public void PostConstruct() {
    this.algorithm = Algorithm.HMAC256(secret);
  }

  public String generateString(UserDetails user) {
    try {

      return JWT.create()
          .withSubject(user.getUsername())
          .withIssuer("Dawood_Spotify")
          .withClaim("roles", user.getAuthorities().stream().toList())
          .withExpiresAt(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
          .withIssuedAt(Instant.now())
          .sign(algorithm);

    } catch (JWTCreationException e) {
      log.error("Error generating authorization token", e);
      throw e;
    }
  }

  public DecodedJWT verifyJwt(String jwt) {
    try {
      JWTVerifier verifier = JWT.require(algorithm)
          .withIssuer("Dawood_Spotify")
          .build();

      return verifier.verify(jwt);

    } catch (JWTVerificationException e) {
      log.error("Error verifying Authorization token", e);
      throw e;
    }
  }

  public String getUsernameFromToken(String jwt) {
    return verifyJwt(jwt).getSubject();
  }

}
