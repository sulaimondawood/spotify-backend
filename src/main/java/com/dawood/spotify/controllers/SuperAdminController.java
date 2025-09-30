package com.dawood.spotify.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.spotify.dtos.ApiResponse;
import com.dawood.spotify.dtos.Meta;
import com.dawood.spotify.dtos.artist.ArtistRequestResponseDTO;
import com.dawood.spotify.dtos.artist.RejectionRequest;
import com.dawood.spotify.dtos.song.SongDTO;
import com.dawood.spotify.entities.Song;
import com.dawood.spotify.enums.ArtistRequestStatus;
import com.dawood.spotify.mappers.SongMapper;
import com.dawood.spotify.services.ArtistRequestService;
import com.dawood.spotify.services.SongService;
import com.dawood.spotify.services.SuperAdminService;
import com.dawood.spotify.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/super")
public class SuperAdminController {

  private final ArtistRequestService artistRequestService;
  private final SuperAdminService superAdminService;
  private final SongService songService;
  private final UserService userService;

  @GetMapping("/me")
  public ResponseEntity<Object> getCurrentArtist() {
    return ResponseEntity.ok().body(ApiResponse.responseBuilder(
        userService.currentLoggedInUser().getArtistProfile(),
        "Your info has been fetched successfully",
        HttpStatus.OK));
  }

  @GetMapping("/become-artist-request")
  public ResponseEntity<Object> getAllBecomeArtistRequests(
      @RequestParam(required = false, defaultValue = "0") int pageNo,
      @RequestParam(required = false, defaultValue = "25") int pageSize,
      @RequestParam(required = false) ArtistRequestStatus status,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) LocalDate startDate,
      @RequestParam(required = false) LocalDate endDate) {

    return ApiResponse.responseBuilder(
        superAdminService.getAllBecomeArtistRequests(pageNo, pageSize, status, keyword, startDate, endDate),
        "All become an artist fetched successfully",
        HttpStatus.OK);
  }

  @GetMapping("/{artistRequestId}/approve-artist-request")
  public ResponseEntity<Object> approveArtistRequest(@PathVariable Long artistRequestId) {

    return ResponseEntity.ok().body(ApiResponse.responseBuilder(
        artistRequestService.approveArtistRequest(artistRequestId),
        "Artist request has been approved",
        HttpStatus.OK));

  }

  @PatchMapping("/{artistRequestId}/reject-artist-request")
  public ResponseEntity<Object> rejectArtistRequest(
      @PathVariable Long artistRequestId,
      @Valid @RequestBody RejectionRequest message) {

    String rejectionReason = message.getRejectionReason();

    ArtistRequestResponseDTO artistRequestResponseDTO = artistRequestService.rejectArtistRequest(artistRequestId,
        rejectionReason);

    return ResponseEntity.ok().body(ApiResponse.responseBuilder(
        artistRequestResponseDTO,
        "Artist request has been rejected",
        HttpStatus.OK));

  }

  // Songs
  @GetMapping("/songs")
  public ResponseEntity<Object> getAllSongs(
      @RequestParam(required = false, defaultValue = "0") int pageNo,
      @RequestParam(required = false, defaultValue = "25") int pageSize,
      @RequestParam(required = false) String keyword,
      @RequestParam(required = false) LocalDate startDate,
      @RequestParam(required = false) LocalDate endDate) {

    LocalDateTime startDateTime = startDate != null ? startDate.atStartOfDay() : null;

    LocalDateTime endDateTime = endDate != null ? endDate.atTime(LocalTime.MAX) : null;

    Page<Song> pagedSongs = songService.getAllSongs(pageNo, pageSize, keyword, startDateTime, endDateTime);

    List<SongDTO> songs = pagedSongs.getContent().stream()
        .map(SongMapper::toDTO).toList();

    Meta meta = new Meta();
    meta.setHasNext(pagedSongs.hasNext());
    meta.setHasPrev(pagedSongs.hasPrevious());
    meta.setPageNo(pagedSongs.getNumber());
    meta.setPageSize(pagedSongs.getSize());
    meta.setTotalPages(pagedSongs.getTotalPages());

    return ApiResponse.responseBuilder(songs, "SOngs successfully fetched", HttpStatus.OK, meta);
  }

  @GetMapping("/songs/{songId}")
  public ResponseEntity<Object> getSongById(@PathVariable Long songId) {

    return ApiResponse.responseBuilder(songService.getSongById(songId), "Song details retrieved", HttpStatus.OK);
  }

  @GetMapping("/songs/{genre}/{artistId}")
  public ResponseEntity<Object> getRelatedSong(@PathVariable("artistId") Long artistId,
      @PathVariable("genre") String genre) {

    return ApiResponse.responseBuilder(songService.getRelatedSongs(genre, artistId), "Related songs retrieved",
        HttpStatus.OK);
  }

  @DeleteMapping("/songs/{songId}")
  public ResponseEntity<Object> deleteArtistSongById(@PathVariable Long songId) {

    superAdminService.deleteArtistSongById(songId);

    return ApiResponse.responseBuilder("",
        "Your song has been removed successfuly",
        HttpStatus.OK);
  }

}
