package com.dawood.spotify.dtos.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponseDTO {

  private Long id;

  private String email;

  private String username;

  private String photoUrl;

}
