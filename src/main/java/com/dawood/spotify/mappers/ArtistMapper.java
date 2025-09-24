package com.dawood.spotify.mappers;

import com.dawood.spotify.dtos.artist.ArtistRequestResponseDTO;
import com.dawood.spotify.entities.ArtistRequest;

public class ArtistMapper {

  public static ArtistRequestResponseDTO toArtistDTO(ArtistRequest request) {

    ArtistRequestResponseDTO artistResponseDTO = new ArtistRequestResponseDTO();
    artistResponseDTO.setId(request.getId());
    artistResponseDTO.setStageName(request.getStageName());
    artistResponseDTO.setBio(request.getBio());
    artistResponseDTO.setGenre(request.getGenre());
    artistResponseDTO.setPhotoUrl(request.getPhotoUrl());
    artistResponseDTO.setStatus(request.getStatus());
    artistResponseDTO.setCreatedAt(request.getCreatedAt());
    artistResponseDTO.setCoverPhotoUrl(request.getCoverPhotoUrl());
    artistResponseDTO.setRejectionReason(request.getRejectionReason());

    return artistResponseDTO;

  }

}
