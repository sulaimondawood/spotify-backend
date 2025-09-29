package com.dawood.spotify.entities;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "artist_profiles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistProfile {

  @Id
  @GeneratedValue
  private Long id;

  private String stageName;

  private String bio;

  private String genre;

  private String photoUrl;

  private String coverPhotoUrl;

  private long monthlyListeners;

  private boolean approved;

  private List<String> socialMediaLinks = new ArrayList<>();

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER, mappedBy = "artistProfile")
  @JsonIgnore
  private List<Song> songs = new ArrayList<>();

  @OneToOne
  @JsonIgnore
  private User user;

  @Embedded
  private Audit audit;
}
