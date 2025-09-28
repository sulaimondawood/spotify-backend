package com.dawood.spotify.services;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawood.spotify.dtos.song.SongDTO;
import com.dawood.spotify.entities.Song;
import com.dawood.spotify.entities.SongUploadJob;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.enums.UploadStatus;
import com.dawood.spotify.exceptions.song.SongNotFoundException;
import com.dawood.spotify.mappers.SongMapper;
import com.dawood.spotify.repositories.SongRepository;
import com.dawood.spotify.repositories.SongUploadJobRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtistService {

  private final UserService userService;
  private final SongRepository songRepository;
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

  public Page<Song> getArtistSongs(
      int pageNo,
      int pageSize,
      String keyword,
      LocalDate startDate,
      LocalDate endDate) {

    LocalDateTime startDateTime = null;
    LocalDateTime endDateTime = null;

    if (startDate != null) {
      startDateTime = startDate.atStartOfDay();
    }
    if (endDate != null) {
      endDateTime = endDate.atTime(LocalTime.MAX);
    }

    Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").descending());

    Page<Song> pagedSongs = songRepository.getAllSongsWithFilters(keyword, startDateTime, endDateTime, pageable);

    return pagedSongs;
  }

  public SongDTO getArtistSongById(Long songId) {

    Song song = songRepository.findById(songId)
        .orElseThrow(() -> new SongNotFoundException());

    return SongMapper.toDTO(song);
  }

}
