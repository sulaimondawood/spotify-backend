package com.dawood.spotify.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dawood.spotify.dtos.ApiResponse;
import com.dawood.spotify.dtos.Meta;
import com.dawood.spotify.dtos.song.SongDTO;
import com.dawood.spotify.dtos.song.UploadSongMessage;
import com.dawood.spotify.entities.Song;
import com.dawood.spotify.entities.SongUploadJob;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.exceptions.user.UserNotFoundException;
import com.dawood.spotify.mappers.SongMapper;
import com.dawood.spotify.messaging.publishers.SongUploadProducer;
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
  private final SongUploadProducer songUploadProducer;
  private final JwtUtils jwtUtils;

  @GetMapping("/me")
  public ResponseEntity<Object> getCurrentArtist() {
    return ResponseEntity.ok().body(ApiResponse.responseBuilder(
        userService.currentLoggedInUser().getArtistProfile(),
        "Your info has been fetched successfully",
        HttpStatus.OK));
  }

  @PostMapping("/upload/song")
  public ResponseEntity<Object> preUploadSong(
      @RequestHeader("Authorization") String authToken,
      @Valid @RequestPart SongDTO payload,
      @RequestPart("trackFile") MultipartFile trackFile,
      @RequestPart("coverArtFile") MultipartFile coverArtFile) throws IOException {

    if (trackFile.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.responseBuilder("",
              "Song file is empty",
              HttpStatus.BAD_REQUEST));
    }
    if (coverArtFile.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(ApiResponse.responseBuilder(
              "",
              "Cover image is empty",
              HttpStatus.BAD_REQUEST));
    }

    Path audioPath = Files.createTempFile("audio_", trackFile.getOriginalFilename());
    Path coverArtPath = Files.createTempFile("cover_", coverArtFile.getOriginalFilename());
    trackFile.transferTo(audioPath);
    coverArtFile.transferTo(coverArtPath);

    SongUploadJob preSongUpload = artistService.preUploadSong(payload);

    String token = authToken.substring(7);
    String email = jwtUtils.getUsernameFromToken(token);

    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new UserNotFoundException());

    UploadSongMessage message = new UploadSongMessage();
    message.setAudioFilePath(audioPath.toString());
    message.setCoverArtFilePath(coverArtPath.toString());
    message.setGenre(payload.getGenre());
    message.setReleaseDate(payload.getReleaseDate());
    message.setSongName(payload.getName());
    message.setUploadId(preSongUpload.getId());
    message.setUserId(user.getId());

    songUploadProducer.convertAndSendMessage(message);

    return ResponseEntity.accepted().body(ApiResponse.responseBuilder(
        preSongUpload,
        "Uploading your song...",
        HttpStatus.ACCEPTED));
  }

  @GetMapping
  public ResponseEntity<?> getArtistSongs(
      @RequestParam(required = false, defaultValue = "0") int pageNo,
      @RequestParam(required = false, defaultValue = "25") int pageSize,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) LocalDate startDate,
      @RequestParam(required = false) LocalDate endDate

  ) {

    Page<Song> pagedSong = artistService.getArtistSongs(pageNo, pageSize, keyword, startDate, endDate);

    List<SongDTO> songs = pagedSong.getContent().stream().map(SongMapper::toDTO).toList();

    Meta meta = new Meta();
    meta.setHasNext(pagedSong.hasNext());
    meta.setHasPrev(pagedSong.hasPrevious());
    meta.setPageNo(pagedSong.getNumber());
    meta.setPageSize(pagedSong.getSize());
    meta.setTotalPages(pagedSong.getTotalPages());

    return ApiResponse.responseBuilder(
        songs,
        "Your songs have been fetched successfully",
        HttpStatus.OK,
        meta);
  }

  @GetMapping("/{songId}")
  public ResponseEntity<Object> getArtistSongById(@PathVariable Long songId) {

    return ApiResponse.responseBuilder(artistService.getArtistSongById(songId),
        "Song details fetched successfully", HttpStatus.OK);
  }

}
