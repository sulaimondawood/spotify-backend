package com.dawood.spotify.dtos.auth;

import com.dawood.spotify.enums.RoleType;
import com.dawood.spotify.validations.RegisterRequestGroup;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AuthRequestDTO {

  @NotBlank(message = "Email is required")
  @Email(message = "Invalid email format", groups = { RegisterRequestGroup.class })
  private String email;

  @NotBlank(message = "Password is required")
  private String password;

  @NotNull(groups = { RegisterRequestGroup.class }, message = "Role is required")
  private RoleType role;

}
