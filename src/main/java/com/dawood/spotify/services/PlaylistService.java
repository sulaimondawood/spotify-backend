package com.dawood.spotify.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawood.spotify.entities.Playlist;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.exceptions.playlist.PlaylistException;
import com.dawood.spotify.repositories.PlaylistRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaylistService {

  private final PlaylistRepository playlistRepository;
  private final UserService userService;

  public void createPlaylist(Map<String, String> payload) {

    if (playlistRepository.existsByName(payload.get("name"))) {
      throw new PlaylistException("Playlist with the same name exists");
    }

    User user = userService.currentLoggedInUser();

    Playlist playlist = new Playlist();
    playlist.setName(payload.get("name"));
    playlist.setUser(user);

    playlistRepository.save(playlist);

  }

  public List<Playlist> getUserPlaylists() {
    return playlistRepository.findByUser(userService.currentLoggedInUser());
  }

  @Transactional
  public void deleteUserPlaylist(Long playlistId) {
    playlistRepository.deleteByUserAndId(userService.currentLoggedInUser(), playlistId);
  }

  public void renamePlaylist(Map<String, String> payload, Long playlistId) {

    Playlist playlist = playlistRepository.findByUserAndId(userService.currentLoggedInUser(), playlistId)
        .orElseThrow(() -> new PlaylistException("Playlist not found"));

    playlist.setName(payload.get("name"));

    playlistRepository.save(playlist);

  }

}
