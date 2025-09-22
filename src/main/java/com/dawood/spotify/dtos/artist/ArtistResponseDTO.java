package com.dawood.spotify.dtos.artist;

import java.util.ArrayList;
import java.util.List;

import com.dawood.spotify.entities.Album;
import com.dawood.spotify.entities.Audit;
import com.dawood.spotify.entities.Song;
import com.dawood.spotify.entities.User;

import lombok.Data;

@Data
public class ArtistResponseDTO {
  private String stageName;

  private String bio;

  private String genre;

  private String photoUrl;

  private String coverPhotoUrl;

  private long monthlyListeners;

  private List<String> socialMediaLinks = new ArrayList<>();

  private List<Album> albums = new ArrayList<>();

  private List<Song> songs = new ArrayList<>();

  private User user;

  private Audit audit = new Audit();

}
