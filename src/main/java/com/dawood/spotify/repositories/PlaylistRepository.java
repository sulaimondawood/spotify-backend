package com.dawood.spotify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.spotify.entities.Playlist;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {

}
