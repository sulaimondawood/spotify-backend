package com.dawood.spotify.entities;

import java.time.LocalDateTime;

import com.dawood.spotify.enums.ArtistRequestStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "artist_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistRequest {

  @Id
  @GeneratedValue
  private Long id;

  @Column(unique = true)
  private String stageName;

  private String bio;

  private String genre;

  @NotBlank(message = "Profile image is required")
  private String photoUrl;

  private String coverPhotoUrl;

  private String rejectionReason;

  @Enumerated(EnumType.STRING)
  private ArtistRequestStatus status;

  @ManyToOne
  private User user;

  private LocalDateTime createdAt;

}
