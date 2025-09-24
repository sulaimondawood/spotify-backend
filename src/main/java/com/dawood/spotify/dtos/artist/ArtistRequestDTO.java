package com.dawood.spotify.dtos.artist;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ArtistRequestDTO {

  @NotBlank(message = "Stage name is required")
  private String stageName;

  @NotBlank(message = "Bio is required")
  private String bio;

  @NotBlank(message = "Pick a genre")
  private String genre;

}
