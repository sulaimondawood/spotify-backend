package com.dawood.spotify.dtos.song;

import java.time.LocalDate;

import com.dawood.spotify.entities.Album;
import com.dawood.spotify.entities.ArtistProfile;
import com.dawood.spotify.entities.Audit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SongDTO {

  private Long id;

  @NotBlank(message = "Song name is required")
  private String name;

  @NotBlank(message = "Add song genre")
  private String genre;

  private String duration;

  private String audioUrl;

  private String coverArtUrl;

  @NotNull(message = "Song release date is required")
  private LocalDate releaseDate;

  private long playCount;

  private ArtistProfile artistProfile;

  private Album album;

  private Audit audit = new Audit();

}
