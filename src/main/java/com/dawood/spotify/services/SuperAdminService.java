package com.dawood.spotify.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.dawood.spotify.dtos.Meta;
import com.dawood.spotify.dtos.artist.ArtistRequestResponseDTO;
import com.dawood.spotify.entities.ArtistRequest;
import com.dawood.spotify.enums.ArtistRequestStatus;
import com.dawood.spotify.mappers.ArtistMapper;
import com.dawood.spotify.repositories.ArtistRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuperAdminService {

  private final ArtistRequestRepository artistRequestRepository;

  public List<ArtistRequestResponseDTO> getAllBecomeArtistRequests(int pageNo, int pageSize, ArtistRequestStatus status,
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

    Meta meta = new Meta();
    meta.setPageNo(artistRequests.getNumber());
    meta.setPageSize(artistRequests.getSize());
    meta.setTotalPages(artistRequests.getTotalPages());
    meta.setHasNext(artistRequests.hasNext());
    meta.setHasPrev(artistRequests.hasPrevious());

    List<ArtistRequestResponseDTO> response = artistRequests.getContent()
        .stream()
        .map(ArtistMapper::toArtistDTO)
        .toList();

    return response;

  }

}
