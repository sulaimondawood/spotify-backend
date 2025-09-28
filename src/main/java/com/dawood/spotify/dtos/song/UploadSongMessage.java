package com.dawood.spotify.dtos.song;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UploadSongMessage {

  private Long uploadId;
  private String songName;
  private String genre;
  private LocalDate releaseDate;
  private String audioFilePath;
  private String coverArtFilePath;
  private Long userId;

}
