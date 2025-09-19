package com.dawood.spotify.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.spotify.dtos.ApiResponse;
import com.dawood.spotify.dtos.auth.AuthRequestDTO;
import com.dawood.spotify.services.AuthService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  public ResponseEntity<Object> register(@Validated @RequestBody AuthRequestDTO request) {

    return ApiResponse.responseBuilder(
        authService.register(request),
        "User registered successfully",
        HttpStatus.CREATED);
  }

}
