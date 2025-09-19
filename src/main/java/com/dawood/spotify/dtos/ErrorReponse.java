package com.dawood.spotify.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorReponse {
  private String message;

  private String error;

  private int status;
}
