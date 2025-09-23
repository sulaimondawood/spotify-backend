package com.dawood.spotify.services;

import org.springframework.stereotype.Service;

import com.dawood.spotify.dtos.artist.ArtistRequestResponseDTO;
import com.dawood.spotify.repositories.ArtistRequestRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {

  private final ArtistRequestRepository artistRequestRepository;

}
