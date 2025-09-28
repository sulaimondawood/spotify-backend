package com.dawood.spotify.services;

import java.io.IOException;
import java.time.Instant;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawood.spotify.dtos.song.SongDTO;
import com.dawood.spotify.entities.SongUploadJob;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.enums.UploadStatus;
import com.dawood.spotify.repositories.SongUploadJobRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtistService {

  private final UserService userService;

  private final SongUploadJobRepository songUploadJobRepository;

  @Transactional
  public SongUploadJob preUploadSong(SongDTO payload)
      throws IOException {

    User currentLoggedInUser = userService.currentLoggedInUser();

    SongUploadJob job = new SongUploadJob();
    job.setCreateAt(Instant.now());
    job.setMessage("Uploading your song...");
    job.setSongName(payload.getName());
    job.setStatus(UploadStatus.IN_PROGRESS);
    job.setUser(currentLoggedInUser);

    return songUploadJobRepository.save(job);

  }

}
