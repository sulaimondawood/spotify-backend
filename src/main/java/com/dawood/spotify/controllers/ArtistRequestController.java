package com.dawood.spotify.controllers;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dawood.spotify.dtos.ApiResponse;
import com.dawood.spotify.dtos.artist.ArtistRequestDTO;
import com.dawood.spotify.dtos.artist.ArtistRequestResponseDTO;
import com.dawood.spotify.services.ArtistRequestService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ArtistRequestController {

  private final ArtistRequestService artistRequestService;

  @PostMapping(value = "/become-an-artist")
  public ResponseEntity<Object> becomeAnArtist(
      @RequestPart(name = "image") MultipartFile image,
      @Valid @RequestPart(name = "data") ArtistRequestDTO request) throws IOException {

    if (image.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.responseBuilder("", "No profile image provided ", HttpStatus.BAD_REQUEST));
    }

    ArtistRequestResponseDTO data = artistRequestService.becomeAnArtist(request, image);

    return ResponseEntity.ok()
        .body(ApiResponse.responseBuilder(data, "You have become an artist", HttpStatus.OK));
  }

}
