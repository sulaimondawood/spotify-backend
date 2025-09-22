package com.dawood.spotify.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dawood.spotify.dtos.ApiResponse;
import com.dawood.spotify.dtos.auth.ActivationRequest;
import com.dawood.spotify.dtos.auth.AuthRequestDTO;
import com.dawood.spotify.dtos.auth.ForgotPasswordDTO;
import com.dawood.spotify.dtos.auth.ResetPasswordDTO;
import com.dawood.spotify.dtos.auth.VerifyCodeDTO;
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

  @PostMapping("activate-account")
  public ResponseEntity<Object> activate(@Valid @RequestBody ActivationRequest request) {

    return ApiResponse.responseBuilder(authService.activateAccount(request.getCode()),
        authService.activateAccount(request.getCode()),
        HttpStatus.OK);
  }

  @PostMapping("/forgot-password")
  public ResponseEntity<Object> forgotPassword(@Valid @RequestBody ForgotPasswordDTO request) {

    authService.forgotPassword(request);

    return ApiResponse.responseBuilder("", "Reset instructions sent to your email.", HttpStatus.OK);
  }

  @PostMapping("/otp")
  public ResponseEntity<Object> validateOtpCode(@Valid @RequestBody VerifyCodeDTO request) {

    authService.verifyCode(request);

    return ApiResponse.responseBuilder("", "OTP verified. Proceed to change your password", HttpStatus.OK);
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Object> resetPassword(@Valid @RequestBody ResetPasswordDTO request) {

    authService.resetPassword(request);

    return ApiResponse.responseBuilder("", "Password reset successfully", HttpStatus.OK);
  }

}
