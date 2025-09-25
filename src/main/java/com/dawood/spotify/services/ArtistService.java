package com.dawood.spotify.services;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.dawood.spotify.dtos.song.SongDTO;
import com.dawood.spotify.entities.ArtistProfile;
import com.dawood.spotify.entities.Song;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.repositories.SongRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArtistService {

  private final SongRepository songRepository;
  private final UserService userService;
  private final CloudinaryService cloudinaryService;

  public SongDTO uploadSong(SongDTO payload, MultipartFile audioFile, MultipartFile coverArtFile) throws IOException {

    User currentLoggedInUser = userService.currentLoggedInUser();
    ArtistProfile artistProfile = currentLoggedInUser.getArtistProfile();

    Map<String, Object> data = cloudinaryService.uploadMultipart(audioFile, payload.getName());

    String audioUrl = data.get("secure_url").toString();

    log.info(audioUrl);
    log.info(data.toString());

    String coverArtUrl = cloudinaryService.uploadMultipart(coverArtFile, payload.getName() + "_cover").get("secure_url")
        .toString();

    Song newSong = new Song();
    newSong.setName(payload.getName());
    newSong.setGenre(payload.getGenre());
    // newSong.setDuration(null);
    newSong.setAudioUrl(audioUrl);
    newSong.setCoverArtUrl(coverArtUrl);
    newSong.setReleaseDate(payload.getReleaseDate());
    newSong.setArtistProfile(artistProfile);

    return null;

  }

}
