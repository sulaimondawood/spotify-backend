package com.dawood.spotify.Exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.dawood.spotify.Exceptions.user.UserAlreadyExists;
import com.dawood.spotify.Exceptions.user.UserException;
import com.dawood.spotify.Exceptions.verification.InvalidCodeException;
import com.dawood.spotify.dtos.ErrorReponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(InvalidCodeException.class)
  public ResponseEntity<ErrorReponse> InvalidCodeExceptionHandler(
      InvalidCodeException ex) {

    ErrorReponse response = new ErrorReponse();

    response.setMessage(ex.getMessage());
    response.setStatus(HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(response);

  }

  @ExceptionHandler(UserException.class)
  public ResponseEntity<ErrorReponse> userExceptionHandler(
      UserException ex) {

    ErrorReponse response = new ErrorReponse();

    response.setMessage(ex.getMessage());
    response.setStatus(HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(response);

  }

  // Mail Exceptions
  @ExceptionHandler(MailException.class)
  public ResponseEntity<ErrorReponse> mailExceptionHandler(
      MailException ex) {

    ErrorReponse response = new ErrorReponse();

    response.setMessage(ex.getMessage());
    response.setStatus(HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(response);

  }

  @ExceptionHandler(MailSendException.class)
  public ResponseEntity<ErrorReponse> mailSendExceptionHandler(
      MailSendException ex) {

    ErrorReponse response = new ErrorReponse();

    response.setMessage(ex.getMessage());
    response.setStatus(HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(response);

  }
  // Mail Exceptions

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> methodArgumentNotValidExceptionHandler(
      MethodArgumentNotValidException ex) {

    Map<String, Object> response = new HashMap<>();

    ex.getBindingResult().getFieldErrors()
        .forEach(err -> {
          response.put(err.getField(), err.getDefaultMessage());
        });

    return ResponseEntity.badRequest().body(response);

  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ErrorReponse> illegalArgumentExceptionHandler(IllegalArgumentException ex) {

    ErrorReponse response = new ErrorReponse();

    response.setMessage(ex.getMessage());
    response.setStatus(HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(response);

  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorReponse> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException ex) {

    ErrorReponse response = new ErrorReponse();

    response.setMessage("Invalid request payload");
    response.setStatus(HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(response);

  }

  @ExceptionHandler(NoResourceFoundException.class)
  public ResponseEntity<ErrorReponse> noResourceFoundExceptionHandler(NoResourceFoundException ex) {

    ErrorReponse response = new ErrorReponse();

    response.setMessage(ex.getMessage());
    response.setStatus(HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.badRequest().body(response);

  }

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
