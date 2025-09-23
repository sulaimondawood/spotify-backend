package com.dawood.spotify.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.dawood.spotify.dtos.artist.ArtistRequestDTO;
import com.dawood.spotify.dtos.artist.ArtistRequestResponseDTO;
import com.dawood.spotify.entities.ArtistRequest;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.enums.ArtistRequestStatus;
import com.dawood.spotify.exceptions.artist.ArtistRequestException;
import com.dawood.spotify.mappers.ArtistMapper;
import com.dawood.spotify.repositories.ArtistRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtistService {

  private final UserService userService;
  private final ArtistRequestRepository artistRequestRepository;

  public ArtistRequestResponseDTO upgradeToArtist(ArtistRequestDTO request) {

    User loggedInUser = userService.currentLoggedInUser();

    if (artistRequestRepository.existsByUser(loggedInUser)) {
      throw new ArtistRequestException("You've already submitted a become artist request");
    }

    ArtistRequest artistRequest = new ArtistRequest();

    artistRequest.setStageName(request.getStageName());
    artistRequest.setBio(request.getBio());
    artistRequest.setGenre(request.getGenre());
    artistRequest.setPhotoUrl(request.getPhotoUrl());
    artistRequest.setCoverPhotoUrl(request.getCoverPhotoUrl());
    artistRequest.setStatus(ArtistRequestStatus.PENDING);
    artistRequest.setUser(loggedInUser);
    artistRequest.setCreatedAt(LocalDateTime.now());

    return ArtistMapper.toArtistDTO(artistRequestRepository.save(artistRequest));

  }

}
