package com.dawood.spotify.dtos.artist;

import lombok.Data;

@Data
public class ArtistRequestResponseDTO {

  private Long id;

  private String stageName;

  private String bio;

  private String genre;

  private String photoUrl;

  private String coverPhotoUrl;

}
