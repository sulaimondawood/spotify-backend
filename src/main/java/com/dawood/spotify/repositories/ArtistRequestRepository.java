package com.dawood.spotify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.spotify.entities.ArtistRequest;
import com.dawood.spotify.entities.User;

public interface ArtistRequestRepository extends JpaRepository<ArtistRequest, Long> {

  ArtistRequest findByUser(User user);

  boolean existsByUser(User user);

}
