package com.dawood.spotify.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.spotify.entities.Song;
import com.dawood.spotify.entities.SongUploadJob;

public interface SongUploadJobRepository extends JpaRepository<SongUploadJob, Long> {

  Optional<SongUploadJob> findBySong(Song song);

}
