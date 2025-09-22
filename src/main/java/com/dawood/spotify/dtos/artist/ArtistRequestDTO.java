package com.dawood.spotify.dtos.artist;

import java.util.List;

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

  @NotBlank(message = "Profile image is required")
  private String photoUrl;

  private String coverPhotoUrl;

  private long monthlyListeners;

  private List<String> socialMediaLinks;
}
