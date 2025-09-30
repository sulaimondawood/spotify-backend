package com.dawood.spotify.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dawood.spotify.entities.ArtistProfile;
import com.dawood.spotify.entities.ArtistRequest;
import com.dawood.spotify.entities.Song;
import com.dawood.spotify.enums.ArtistRequestStatus;
import com.dawood.spotify.exceptions.song.SongNotFoundException;
import com.dawood.spotify.repositories.ArtistRequestRepository;
import com.dawood.spotify.repositories.SongRepository;
import com.dawood.spotify.repositories.SongUploadJobRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuperAdminService {

  private final ArtistRequestRepository artistRequestRepository;
  private final SongRepository songRepository;
  private final SongUploadJobRepository songUploadJobRepository;

  public Page<ArtistRequest> getAllBecomeArtistRequests(int pageNo, int pageSize, ArtistRequestStatus status,
      String keyword, LocalDate startDate, LocalDate endDate) {

    LocalDateTime starDateTime = null;
    if (startDate != null) {
      starDateTime = startDate.atStartOfDay();
    }

    LocalDateTime endDateTime = null;
    if (endDate != null) {
      endDateTime = endDate.atTime(LocalTime.MAX);
    }

    Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by("createdAt").ascending());

    Page<ArtistRequest> artistRequests = artistRequestRepository.getAllArtistWithFilters(
        status,
        keyword,
        starDateTime,
        endDateTime,
        pageable);

    return artistRequests;

  }

  public void deleteArtistSongById(Long songId) {

    Song song = songRepository.findById(songId)
        .orElseThrow(() -> new SongNotFoundException());

    songUploadJobRepository.findBySong(song).ifPresent(songUploadJobRepository::delete);

    ArtistProfile artistProfile = song.getArtistProfile();
    artistProfile.getSongs().remove(song);

    songRepository.delete(song);

  }

}
