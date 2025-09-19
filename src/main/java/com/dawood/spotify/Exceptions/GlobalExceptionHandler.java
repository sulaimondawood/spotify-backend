package com.dawood.spotify.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.dawood.spotify.dtos.ErrorReponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(UserAlreadyExists.class)
  public ResponseEntity<ErrorReponse> userAlreadyExistsHandler(UserAlreadyExists ex) {

    ErrorReponse response = new ErrorReponse();

    response.setMessage(ex.getMessage());
    response.setStatus(HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(response);

  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorReponse> genericExceptionHandler(Exception ex) {

    ErrorReponse response = new ErrorReponse();

    response.setError("Internal Server Error");
    response.setMessage("Something went wrong");
    response.setStatus(HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.internalServerError().body(response);

  }

}
