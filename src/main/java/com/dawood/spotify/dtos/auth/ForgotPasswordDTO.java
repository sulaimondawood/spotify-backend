package com.dawood.spotify.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForgotPasswordDTO {

  @NotBlank(message = "Email is required")
  private String email;

}
