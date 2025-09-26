package com.dawood.spotify.controllers;

import java.io.IOException;

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

    return ResponseEntity.ok().body(ApiResponse.responseBuilder(
        artistService.uploadSong(payload, trackFile, coverArtFile),
        "Your song has been uploaded",
        HttpStatus.OK));
  }

}
