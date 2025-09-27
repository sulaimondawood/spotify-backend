package com.dawood.spotify.entities;

import java.time.Instant;

import com.dawood.spotify.enums.UploadStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "song_upload_jobs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SongUploadJob {

  @Id
  @GeneratedValue
  private Long id;

  private String songName;

  @Enumerated(EnumType.STRING)
  private UploadStatus status;

  private String message;

  @ManyToOne(fetch = FetchType.LAZY)
  private User user;

  @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  private Song song;

  private Instant createAt = Instant.now();

}
