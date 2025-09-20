package com.dawood.spotify.dtos;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiResponse {

  public static ResponseEntity<Object> responseBuilder(
      Object data,
      String message,
      HttpStatus status) {

    Map<String, Object> response = Map.of(
        "message", message,
        "data", data,
        "status", status.value());

    return ResponseEntity.status(status).body(response);

  }

}
