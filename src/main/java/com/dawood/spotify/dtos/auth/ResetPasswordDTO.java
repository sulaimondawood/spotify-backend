package com.dawood.spotify.dtos.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordDTO {

  @NotNull(message = "Reset code is required")
  private int code;

  @NotBlank(message = "Email is required")
  private String email;

  @NotBlank(message = "Password is required")
  private String newPassword;

  @NotBlank(message = "Confirm password is required")
  private String confirmPassword;

}
