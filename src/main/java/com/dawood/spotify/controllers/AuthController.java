package com.dawood.spotify.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.spotify.dtos.ApiResponse;
import com.dawood.spotify.dtos.auth.AuthRequestDTO;
import com.dawood.spotify.services.AuthService;
import com.dawood.spotify.validations.RegisterRequestGroup;

import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  public ResponseEntity<Object> register(
      @Validated(value = { Default.class, RegisterRequestGroup.class }) @RequestBody AuthRequestDTO request) {

    return ApiResponse.responseBuilder(
        authService.register(request),
        "User registered successfully",
        HttpStatus.CREATED);
  }

  @PostMapping("/login")
  public ResponseEntity<Object> login(@Valid @RequestBody AuthRequestDTO request) {
    return ApiResponse.responseBuilder(authService.login(request), "Login successfull", HttpStatus.OK);
  }

}
