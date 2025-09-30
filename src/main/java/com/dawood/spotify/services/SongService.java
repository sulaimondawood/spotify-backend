package com.dawood.spotify.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dawood.spotify.dtos.song.SongDTO;
import com.dawood.spotify.entities.Song;
import com.dawood.spotify.exceptions.song.SongNotFoundException;
import com.dawood.spotify.mappers.SongMapper;
import com.dawood.spotify.repositories.SongRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SongService {

  private final SongRepository songRepository;

  public Page<Song> getAllSongs(int pageNo, int pageSize, String keyword, LocalDateTime starDate,
      LocalDateTime endDate) {

    Pageable pageable = PageRequest.of(pageNo, pageSize);

    Page<Song> pagedSongs = songRepository.getAllSongsWithFilters(keyword, starDate, endDate, null, pageable);

    return pagedSongs;

  }

  public SongDTO getSongById(Long songId) {

    Song song = songRepository.findById(songId).orElseThrow(() -> new SongNotFoundException());

    return SongMapper.toDTO(song);
  }

  public List<SongDTO> getRelatedSongs(String genre, Long artistId) {

    List<Song> songs = songRepository.getRelatedSongs(genre, artistId);

    return songs.stream().map(SongMapper::toDTO).toList();

  }

}
