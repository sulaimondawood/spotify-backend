package com.dawood.spotify.services;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dawood.spotify.dtos.song.SongDTO;
import com.dawood.spotify.entities.ArtistProfile;
import com.dawood.spotify.entities.Song;
import com.dawood.spotify.entities.SongUploadJob;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.enums.UploadStatus;
import com.dawood.spotify.mappers.SongMapper;
import com.dawood.spotify.repositories.SongRepository;
import com.dawood.spotify.repositories.SongUploadJobRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtistService {

  private final SongRepository songRepository;
  private final UserService userService;
  private final CloudinaryService cloudinaryService;
  private final SongUploadJobRepository songUploadJobRepository;

  @Transactional
  public SongUploadJob uploadSong(SongDTO payload)
      throws IOException {

    User currentLoggedInUser = userService.currentLoggedInUser();
    ArtistProfile artistProfile = currentLoggedInUser.getArtistProfile();

    SongUploadJob job = new SongUploadJob();
    job.setCreateAt(Instant.now());
    job.setMessage("Uploading your song...");
    job.setSongName(payload.getName());
    job.setStatus(UploadStatus.IN_PROGRESS);
    job.setUser(currentLoggedInUser);

    return songUploadJobRepository.save(job);

  }

}
