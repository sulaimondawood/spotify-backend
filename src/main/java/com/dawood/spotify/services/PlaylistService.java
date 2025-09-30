package com.dawood.spotify.services;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawood.spotify.dtos.playlist.PlaylistSongRequest;
import com.dawood.spotify.entities.Playlist;
import com.dawood.spotify.entities.Song;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.exceptions.playlist.PlaylistException;
import com.dawood.spotify.repositories.PlaylistRepository;
import com.dawood.spotify.repositories.SongRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlaylistService {

  private final PlaylistRepository playlistRepository;
  private final UserService userService;
  private final SongRepository songRepository;

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

  public void addSongsToPlaylist(PlaylistSongRequest request) {

    Playlist playlist = playlistRepository.findByUserAndId(userService.currentLoggedInUser(), request.getPlaylistId())
        .orElseThrow(() -> new PlaylistException("Playlist does not exist"));

    List<Song> songs = songRepository.findAllById(request.getSongs());

    songs.forEach(song -> {
      if (!playlist.getSongs().contains(song)) {
        playlist.getSongs().add(song);
      }
    });

    playlistRepository.save(playlist);

  }

  public void removeSongFromPlaylist(PlaylistSongRequest request) {

    Playlist playlist = playlistRepository.findByUserAndId(userService.currentLoggedInUser(), request.getPlaylistId())
        .orElseThrow(() -> new PlaylistException("Playlist does not exist"));

    List<Song> songs = songRepository.findAllById(request.getSongs());

    songs.forEach((song) -> {
      if (playlist.getSongs().contains(song)) {
        playlist.getSongs().remove(song);
      }
    });

    playlistRepository.save(playlist);

  }

}
