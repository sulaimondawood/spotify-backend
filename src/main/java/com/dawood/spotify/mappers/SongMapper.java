package com.dawood.spotify.mappers;

import com.dawood.spotify.dtos.song.SongDTO;
import com.dawood.spotify.entities.Song;
import com.dawood.spotify.utils.SongUtils;

public class SongMapper {

  public static SongDTO toDTO(Song song) {

    SongDTO responseDTO = new SongDTO();

    responseDTO.setId(song.getId());
    responseDTO.setName(song.getName());
    responseDTO.setGenre(song.getGenre());
    responseDTO.setDuration(SongUtils.formatTrackDuration(song.getDuration()));
    responseDTO.setAudioUrl(song.getAudioUrl());
    responseDTO.setCoverArtUrl(song.getCoverArtUrl());
    responseDTO.setReleaseDate(song.getReleaseDate());
    responseDTO.setPlayCount(song.getPlayCount());
    responseDTO.setArtistProfile(song.getArtistProfile());

    return responseDTO;

  }

}
