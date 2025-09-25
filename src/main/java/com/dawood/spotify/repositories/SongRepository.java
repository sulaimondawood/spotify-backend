package com.dawood.spotify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.spotify.entities.Song;

public interface SongRepository extends JpaRepository<Song, Long> {

}
