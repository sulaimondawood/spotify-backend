package com.dawood.spotify.utils;

import java.io.IOException;

import org.springframework.http.MediaType;

import com.dawood.spotify.dtos.ErrorReponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public class ErrorResponseWriter {
  public static void write(HttpServletResponse response, String message, int status)
      throws JsonProcessingException, IOException {

    ObjectMapper mapper = new ObjectMapper();

    ErrorReponse error = new ErrorReponse();

    error.setMessage(message);
    error.setStatus(status);

    response.setStatus(status);
    response.setContentType(MediaType.APPLICATION_JSON.toString());
    response.getWriter().write(mapper.writeValueAsString(error));
  }

}
