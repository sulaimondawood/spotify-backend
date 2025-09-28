package com.dawood.spotify.dtos;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
public class ApiResponse<T> {

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

  public static ResponseEntity<Object> responseBuilder(
      Object data,
      String message,
      HttpStatus status,
      Meta meta) {

    Map<String, Object> response = Map.of(
        "message", message,
        "data", data,
        "status", status.value(),
        "meta", meta);
    return ResponseEntity.status(status).body(response);

  }

}
