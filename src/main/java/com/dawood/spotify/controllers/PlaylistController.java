package com.dawood.spotify.controllers;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.spotify.dtos.ApiResponse;
import com.dawood.spotify.exceptions.playlist.PlaylistException;
import com.dawood.spotify.services.PlaylistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/playlists")
@RequiredArgsConstructor
public class PlaylistController {

  private final PlaylistService playlistService;

  @PostMapping
  public ResponseEntity<Object> createPlaylist(@RequestBody Map<String, String> payload) {

    if (payload.get("name") == null) {
      throw new PlaylistException("Playlist name is required");
    }

    playlistService.createPlaylist(payload);

    return ApiResponse.responseBuilder(
        "",
        "Your playlist have been created",
        HttpStatus.CREATED);

  }

  @GetMapping
  public ResponseEntity<Object> getUserPlaylists() {

    return ApiResponse.responseBuilder(
        playlistService.getUserPlaylists(),
        "Your playlist have been fetched",
        HttpStatus.OK);
  }

  @DeleteMapping("/{playlistId}")
  public ResponseEntity<Object> getUserPlaylists(@PathVariable Long playlistId) {

    playlistService.deleteUserPlaylist(playlistId);

    return ApiResponse.responseBuilder(
        "",
        "Your playlist was removed successfully",
        HttpStatus.OK);
  }

  @PatchMapping("/{playlistId}")
  public ResponseEntity<Object> renamePlaylist(
      @RequestBody Map<String, String> payload,
      @PathVariable Long playlistId) {

    if (payload.get("name").isEmpty()) {
      throw new PlaylistException("Playlist new name is required");
    }

    playlistService.renamePlaylist(payload, playlistId);

    return ApiResponse.responseBuilder(
        "",
        "Your playlist rename was successfull",
        HttpStatus.OK);

  }

}
