package com.dawood.spotify.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "playlists")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Playlist {

  @Id
  @GeneratedValue
  private Long id;

  @ManyToMany
  @JoinTable(joinColumns = @JoinColumn(name = "playlist_id"), inverseJoinColumns = @JoinColumn(name = "song_id"), name = "playlist_song")
  private List<Song> songs = new ArrayList<>();

  @Embedded
  Audit audit;

}
