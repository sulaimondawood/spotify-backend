package com.dawood.spotify.entities;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "songs")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Song {

  @Id
  @GeneratedValue
  private Long id;

  private String name;

  private String genre;

  private double duration;

  @Column(nullable = false)
  private String audioUrl;

  private String coverArtUrl;

  private LocalDate releaseDate;

  private long playCount;

  @ManyToOne
  private ArtistProfile artistProfile;

  @ManyToOne
  private Album album;

  @Embedded
  private Audit audit = new Audit();

}
