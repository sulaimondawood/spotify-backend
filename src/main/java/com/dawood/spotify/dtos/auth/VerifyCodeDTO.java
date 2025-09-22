package com.dawood.spotify.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VerifyCodeDTO {

  @NotNull(message = "Reset code is required")
  private int code;

  @NotBlank(message = "Email is required")
  private String email;

}
