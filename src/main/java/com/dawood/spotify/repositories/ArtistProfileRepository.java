package com.dawood.spotify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.spotify.entities.ArtistProfile;

public interface ArtistProfileRepository extends JpaRepository<ArtistProfile, Long> {

}
