package com.dawood.spotify.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.spotify.dtos.ApiResponse;
import com.dawood.spotify.services.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/artists")
@RequiredArgsConstructor
public class ArtistController {

  private final UserService userService;

  @GetMapping
  public ResponseEntity<Object> getCurrentArtist() {
    return ResponseEntity.ok().body(ApiResponse.responseBuilder(
        userService.currentLoggedInUser(),
        "Your info has been fetched successfully",
        HttpStatus.OK));
  }

}
