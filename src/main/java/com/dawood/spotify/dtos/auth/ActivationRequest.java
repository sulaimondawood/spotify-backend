package com.dawood.spotify.dtos.auth;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ActivationRequest {

  @NotNull(message = "Activation code is required")
  private int code;
}
