package com.dawood.spotify.mappers;

import com.dawood.spotify.dtos.song.SongDTO;
import com.dawood.spotify.entities.Song;

public class SongMapper {

  public SongDTO toDTO(Song song) {

    SongDTO responseDTO = new SongDTO();

    responseDTO.setId(song.getId());
    responseDTO.setName(song.getName());
    responseDTO.setGenre(song.getGenre());
    responseDTO.setDuration(song.getDuration());
    responseDTO.setAudioUrl(song.getAudioUrl());
    responseDTO.setCoverArtUrl(song.getCoverArtUrl());
    responseDTO.setReleaseDate(song.getReleaseDate());
    responseDTO.setPlayCount(song.getPlayCount());
    responseDTO.setArtistProfile(song.getArtistProfile());
    responseDTO.setAlbum(song.getAlbum());
    responseDTO.setAudit(song.getAudit());

    return responseDTO;

  }

}
