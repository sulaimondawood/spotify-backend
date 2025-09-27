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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dawood.spotify.dtos.ApiResponse;
import com.dawood.spotify.dtos.song.SongDTO;
import com.dawood.spotify.services.ArtistService;
import com.dawood.spotify.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistController {

  private final UserService userService;
  private final ArtistService artistService;

  @GetMapping
  public ResponseEntity<Object> getCurrentArtist() {
    return ResponseEntity.ok().body(ApiResponse.responseBuilder(
        userService.currentLoggedInUser().getArtistProfile(),
        "Your info has been fetched successfully",
        HttpStatus.OK));
  }

  @PostMapping("/upload/song")
  public ResponseEntity<Object> uploadSong(
      @Valid @RequestPart SongDTO payload,
      @RequestPart("trackFile") MultipartFile trackFile,
      @RequestPart("coverArtFile") MultipartFile coverArtFile) throws IOException {

    Path audioPath = Files.createTempFile("audio_", trackFile.getOriginalFilename());
    Path coverArtPath = Files.createTempFile("cover_", coverArtFile.getOriginalFilename());

    trackFile.transferTo(audioPath);
    coverArtFile.transferTo(coverArtPath);

    return ResponseEntity.accepted().body(ApiResponse.responseBuilder(
        artistService.uploadSong(payload),
        "Uploading your song...",
        HttpStatus.ACCEPTED));
  }

}
