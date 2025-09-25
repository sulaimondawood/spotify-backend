package com.dawood.spotify.controllers;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.spotify.dtos.ApiResponse;
import com.dawood.spotify.dtos.artist.ArtistRequestResponseDTO;
import com.dawood.spotify.dtos.artist.RejectionRequest;
import com.dawood.spotify.enums.ArtistRequestStatus;
import com.dawood.spotify.services.ArtistRequestService;
import com.dawood.spotify.services.SuperAdminService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/super")
public class SuperAdminController {

  private final ArtistRequestService artistRequestService;
  private final SuperAdminService superAdminService;

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

}
