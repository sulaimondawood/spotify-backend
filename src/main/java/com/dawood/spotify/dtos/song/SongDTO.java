package com.dawood.spotify.dtos.song;

import java.time.LocalDate;

import com.dawood.spotify.entities.Album;
import com.dawood.spotify.entities.ArtistProfile;
import com.dawood.spotify.entities.Audit;

import lombok.Data;

@Data
public class SongDTO {

  private Long id;

  private String name;

  private String genre;

  private int duration;

  private String audioUrl;

  private String coverArtUrl;

  private LocalDate releaseDate;

  private long playCount;

  private ArtistProfile artistProfile;

  private Album album;

  private Audit audit = new Audit();

}
