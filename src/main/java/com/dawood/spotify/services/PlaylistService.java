package com.dawood.spotify.services;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.dawood.spotify.entities.Playlist;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.repositories.PlaylistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaylistService {

  private final PlaylistRepository playlistRepository;
  private final UserService userService;

  public void createPlaylist(Map<String, String> payload) {

    User user = userService.currentLoggedInUser();

    Playlist playlist = new Playlist();
    playlist.setName(payload.get("name"));
    playlist.setUser(user);

    playlistRepository.save(playlist);

  }

}
