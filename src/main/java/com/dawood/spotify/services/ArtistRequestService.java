package com.dawood.spotify.services;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawood.spotify.dtos.artist.ArtistRequestDTO;
import com.dawood.spotify.dtos.artist.ArtistRequestResponseDTO;
import com.dawood.spotify.entities.ArtistProfile;
import com.dawood.spotify.entities.ArtistRequest;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.enums.ArtistRequestStatus;
import com.dawood.spotify.enums.RoleType;
import com.dawood.spotify.exceptions.artist.ArtistRequestException;
import com.dawood.spotify.mappers.ArtistMapper;
import com.dawood.spotify.repositories.ArtistProfileRepository;
import com.dawood.spotify.repositories.ArtistRequestRepository;
import com.dawood.spotify.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ArtistRequestService {

  private final UserService userService;
  private final ArtistRequestRepository artistRequestRepository;
  private final UserRepository userRepository;
  private final ArtistProfileRepository artistProfileRepository;

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

  @Transactional
  public ArtistRequestResponseDTO approveArtistRequest(Long artistRequestId) {

    ArtistRequest artistRequest = artistRequestRepository.findById(artistRequestId)
        .orElseThrow(() -> new ArtistRequestException("Artist request not found!"));

    if (!artistRequest.getStatus().equals(ArtistRequestStatus.PENDING)) {
      throw new ArtistRequestException("Request already processed!");
    }

    User user = artistRequest.getUser();
    user.getRoles().add(RoleType.ARTIST);
    userRepository.save(user);

    ArtistProfile artistProfile = new ArtistProfile();
    artistProfile.setStageName(artistRequest.getStageName());
    artistProfile.setBio(artistRequest.getBio());
    artistProfile.setGenre(artistRequest.getGenre());
    artistProfile.setPhotoUrl(artistRequest.getPhotoUrl());
    artistProfile.setCoverPhotoUrl(artistRequest.getCoverPhotoUrl());
    artistProfile.setApproved(true);
    artistProfile.setUser(user);

    artistProfileRepository.save(artistProfile);

    artistRequest.setStatus(ArtistRequestStatus.APPROVED);
    ArtistRequest savedArtistRequest = artistRequestRepository.save(artistRequest);

    return ArtistMapper.toArtistDTO(savedArtistRequest);

  }

  public ArtistRequestResponseDTO rejectArtistRequest(Long artistRequestId, String rejectionReason) {

    ArtistRequest artistRequest = artistRequestRepository.findById(artistRequestId)
        .orElseThrow(() -> new ArtistRequestException("Artist request not found!"));

    if (!artistRequest.getStatus().equals(ArtistRequestStatus.PENDING)) {
      throw new ArtistRequestException("Request already processed!");
    }

    artistRequest.setRejectionReason(rejectionReason);
    artistRequest.setStatus(ArtistRequestStatus.REJECTED);

    ArtistRequest savedArtistRequest = artistRequestRepository.save(artistRequest);

    return ArtistMapper.toArtistDTO(savedArtistRequest);
  }

}
