package com.dawood.spotify.dtos.auth;

import com.dawood.spotify.validations.RegisterRequestGroup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AuthRequestDTO {

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format")
  private String email;

  @NotBlank(message = "Fullname is required", groups = { RegisterRequestGroup.class })
  private String fullname;

  @NotBlank(message = "Password is required")
  private String password;

}
