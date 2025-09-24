package com.dawood.spotify.controllers;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.spotify.dtos.ApiResponse;
import com.dawood.spotify.enums.ArtistRequestStatus;
import com.dawood.spotify.services.SuperAdminService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/super")
public class AdminController {

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

}
