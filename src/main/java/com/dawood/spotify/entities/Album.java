package com.dawood.spotify.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "albums")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Album {

  @Id
  @GeneratedValue
  private Long id;

  private String title;

  private String coverArtUrl;

  private LocalDate releaseDate;

  private String genre;

  private String description;

  private long playCount;

  @ManyToOne
  private ArtistProfile artistProfile;

  @OneToMany(mappedBy = "album")
  List<Song> songs = new ArrayList<>();

  @Embedded
  private Audit audit = new Audit();

}
