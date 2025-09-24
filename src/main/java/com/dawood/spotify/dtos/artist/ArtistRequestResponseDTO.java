package com.dawood.spotify.dtos.artist;

import java.time.LocalDateTime;

import com.dawood.spotify.enums.ArtistRequestStatus;

import lombok.Data;

@Data
public class ArtistRequestResponseDTO {

  private Long id;

  private String stageName;

  private String bio;

  private String genre;

  private String photoUrl;

  private String coverPhotoUrl;

  private ArtistRequestStatus status;

  private String rejectionReason;

  private LocalDateTime createdAt;

}
