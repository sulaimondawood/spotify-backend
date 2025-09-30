package com.dawood.spotify.services;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.dawood.spotify.dtos.artist.ArtistRequestDTO;
import com.dawood.spotify.dtos.artist.ArtistRequestResponseDTO;
import com.dawood.spotify.entities.ArtistProfile;
import com.dawood.spotify.entities.ArtistRequest;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.enums.ArtistRequestStatus;
import com.dawood.spotify.enums.RoleType;
import com.dawood.spotify.exceptions.artist.ArtistException;
import com.dawood.spotify.exceptions.artist.ArtistRequestException;
import com.dawood.spotify.mappers.ArtistMapper;
import com.dawood.spotify.messaging.publishers.MQEmailProducer;
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
  private final CloudinaryService cloudinaryService;
  private final MQEmailProducer mqEmailProducer;

  public ArtistRequestResponseDTO becomeAnArtist(ArtistRequestDTO request, MultipartFile file) throws IOException {

    User loggedInUser = userService.currentLoggedInUser();

    if (artistProfileRepository.existsByStageName(request.getStageName())) {
      throw new ArtistException("Artist with stage name already exists");
    }

    if (artistRequestRepository.existsByUserAndStatus(loggedInUser, ArtistRequestStatus.PENDING)) {
      throw new ArtistRequestException("You've already submitted a become artist request");
    }

    String photoUrl = cloudinaryService.uploadMultipart(file, request.getStageName())
        .get("secure_url")
        .toString();

    ArtistRequest artistRequest = new ArtistRequest();

    artistRequest.setStageName(request.getStageName());
    artistRequest.setBio(request.getBio());
    artistRequest.setGenre(request.getGenre());
    artistRequest.setPhotoUrl(photoUrl);
    artistRequest.setStatus(ArtistRequestStatus.PENDING);
    artistRequest.setUser(loggedInUser);
    artistRequest.setCreatedAt(LocalDateTime.now());

    String body = """

        Hello there,

        Your request to become an artist is currently being processed.

        Kindly exercise patient, while the team review your application.

        Regards,
        Dawood team.

          """;

    mqEmailProducer.sendMessage(loggedInUser.getEmail(), "Become an Artist Request", body);

    return ArtistMapper.toArtistDTO(artistRequestRepository.save(artistRequest));

  }

  @Transactional
  public ArtistRequestResponseDTO approveArtistRequest(Long artistRequestId) {

    ArtistRequest artistRequest = artistRequestRepository.findById(artistRequestId)
        .orElseThrow(() -> new ArtistRequestException("Artist request not found!"));

    if (artistProfileRepository.existsByStageName(artistRequest.getStageName())) {
      throw new ArtistException("Artist with stage name already exists");
    }

    if (!artistRequest.getStatus().equals(ArtistRequestStatus.PENDING)) {
      throw new ArtistRequestException("Request already processed!");
    }

    User user = artistRequest.getUser();
    user.getRoles().add(RoleType.ARTIST);

    ArtistProfile artistProfile = new ArtistProfile();
    artistProfile.setStageName(artistRequest.getStageName());
    artistProfile.setBio(artistRequest.getBio());
    artistProfile.setGenre(artistRequest.getGenre());
    artistProfile.setPhotoUrl(artistRequest.getPhotoUrl());
    artistProfile.setApproved(true);
    artistProfile.setUser(user);

    artistProfileRepository.save(artistProfile);

    user.setArtistProfile(artistProfile);
    userRepository.save(user);

    artistRequest.setStatus(ArtistRequestStatus.APPROVED);
    ArtistRequest savedArtistRequest = artistRequestRepository.save(artistRequest);

    String body = """

        Hello there,

        Your request to become an artist was approved.

        You can now proceed to uploading songs.

        Regards,
        Dawood team.

          """;

    mqEmailProducer.sendMessage(user.getEmail(), "Become an Artist Request - Approved", body);

    return ArtistMapper.toArtistDTO(savedArtistRequest);

  }

  public ArtistRequestResponseDTO rejectArtistRequest(Long artistRequestId, String rejectionReason) {

    User loggedInUser = userService.currentLoggedInUser();

    ArtistRequest artistRequest = artistRequestRepository.findById(artistRequestId)
        .orElseThrow(() -> new ArtistRequestException("Artist request not found!"));

    if (!artistRequest.getStatus().equals(ArtistRequestStatus.PENDING)) {
      throw new ArtistRequestException("Request already processed!");
    }

    artistRequest.setRejectionReason(rejectionReason);
    artistRequest.setStatus(ArtistRequestStatus.REJECTED);

    ArtistRequest savedArtistRequest = artistRequestRepository.save(artistRequest);

    String body = """

        Hello there,

        Your request to become an artist was rejected.

        You can try again some other time.

        Regards,
        Dawood team.

          """;

    mqEmailProducer.sendMessage(loggedInUser.getEmail(), "Become an Artist Request - Rejected", body);

    return ArtistMapper.toArtistDTO(savedArtistRequest);
  }

}
