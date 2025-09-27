package com.dawood.spotify.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.spotify.entities.SongUploadJob;

public interface SongUploadJobRepository extends JpaRepository<SongUploadJob, Long> {

}
