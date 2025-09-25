package com.dawood.spotify.dtos.artist;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

@Data
public class ArtistResponseDTO {

  private Long id;

  private String stageName;

  private String bio;

  private String genre;

  private String photoUrl;

  private String coverPhotoUrl;

  private long monthlyListeners;

  private boolean approved;

  private List<String> socialMediaLinks = new ArrayList<>();
}
