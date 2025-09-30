package com.dawood.spotify.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.spotify.dtos.ApiResponse;
import com.dawood.spotify.dtos.Meta;
import com.dawood.spotify.dtos.song.SongDTO;
import com.dawood.spotify.entities.Song;
import com.dawood.spotify.mappers.SongMapper;
import com.dawood.spotify.services.SongService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/songs")
public class SongController {

  private final SongService songService;

  @GetMapping
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

  @GetMapping("/{songId}")
  public ResponseEntity<Object> getSongById(@PathVariable Long songId) {

    return ApiResponse.responseBuilder(songService.getSongById(songId), "Song details retrieved", HttpStatus.OK);
  }

  @GetMapping("/{genre}/{artistId}")
  public ResponseEntity<Object> getRelatedSong(@PathVariable("artistId") Long artistId,
      @PathVariable("genre") String genre) {

    return ApiResponse.responseBuilder(songService.getRelatedSongs(genre, artistId), "Related songs retrieved",
        HttpStatus.OK);
  }

}
