package com.dawood.spotify.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.spotify.entities.Playlist;
import com.dawood.spotify.entities.User;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

  List<Playlist> findByUser(User user);

  Optional<Playlist> findByUserAndId(User user, Long id);

  boolean existsByName(String name);

  void deleteByUserAndId(User user, Long id);

}
