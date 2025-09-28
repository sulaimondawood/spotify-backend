package com.dawood.spotify.controllers;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dawood.spotify.dtos.ApiResponse;
import com.dawood.spotify.dtos.song.SongDTO;
import com.dawood.spotify.dtos.song.UploadSongMessage;
import com.dawood.spotify.entities.SongUploadJob;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.exceptions.user.UserNotFoundException;
import com.dawood.spotify.messaging.publishers.SongUploadProducer;
import com.dawood.spotify.repositories.SongUploadJobRepository;
import com.dawood.spotify.repositories.UserRepository;
import com.dawood.spotify.services.ArtistService;
import com.dawood.spotify.services.UserService;
import com.dawood.spotify.utils.JwtUtils;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistController {

  private final UserService userService;
  private final UserRepository userRepository;
  private final ArtistService artistService;
  private final SongUploadJobRepository songUploadJobRepository;
  private final SongUploadProducer songUploadProducer;
  private final JwtUtils jwtUtils;

  @GetMapping
  public ResponseEntity<Object> getCurrentArtist() {
    return ResponseEntity.ok().body(ApiResponse.responseBuilder(
        userService.currentLoggedInUser().getArtistProfile(),
        "Your info has been fetched successfully",
        HttpStatus.OK));
  }

  @PostMapping("/upload/song")
  public ResponseEntity<Object> uploadSong(
      @RequestHeader("Authorization") String authToken,
      @Valid @RequestPart SongDTO payload,
      @RequestPart("trackFile") MultipartFile trackFile,
      @RequestPart("coverArtFile") MultipartFile coverArtFile) throws IOException {

    if (trackFile.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.responseBuilder("", "Song file is empty", HttpStatus.BAD_REQUEST));
    }
    if (coverArtFile.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.responseBuilder("", "Cover image is empty", HttpStatus.BAD_REQUEST));
    }

    Path audioPath = Files.createTempFile("audio_", trackFile.getOriginalFilename());
    Path coverArtPath = Files.createTempFile("cover_", coverArtFile.getOriginalFilename());
    trackFile.transferTo(audioPath);
    coverArtFile.transferTo(coverArtPath);

    SongUploadJob preSongUpload = artistService.uploadSong(payload);

    String token = authToken.substring(7);
    String email = jwtUtils.getUsernameFromToken(token);

    User user = userRepository.findByEmail(email).orElseThrow(() -> new UserNotFoundException());

    UploadSongMessage message = new UploadSongMessage();
    message.setAudioFilePath(audioPath.toString());
    message.setCoverArtFilePath(coverArtPath.toString());
    message.setGenre(payload.getGenre());
    message.setReleaseDate(payload.getReleaseDate());
    message.setSongName(payload.getName());
    message.setUploadId(preSongUpload.getId());
    message.setUserId(user.getId());

    songUploadProducer.sendMessage(message);

    return ResponseEntity.accepted().body(ApiResponse.responseBuilder(
        preSongUpload,
        "Uploading your song...",
        HttpStatus.ACCEPTED));
  }

}
