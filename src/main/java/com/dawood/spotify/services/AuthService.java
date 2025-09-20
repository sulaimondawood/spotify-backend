package com.dawood.spotify.services;

import java.time.LocalDateTime;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dawood.spotify.Exceptions.UserAlreadyExists;
import com.dawood.spotify.Exceptions.UserNotFoundException;
import com.dawood.spotify.dtos.auth.AuthRequestDTO;
import com.dawood.spotify.dtos.auth.RegisterResponseDTO;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.entities.VerificationCode;
import com.dawood.spotify.messaging.publisher.MQEmailProducer;
import com.dawood.spotify.repositories.UserRepository;
import com.dawood.spotify.repositories.VerificationCodeRepository;
import com.dawood.spotify.utils.JwtUtils;
import com.dawood.spotify.utils.VerificationUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtils jwtUtils;
  private final CustomUserDetailsService customUserDetailsService;
  private final VerificationCodeRepository verificationCodeRepository;
  private final MQEmailProducer mqEmailProducer;

  public RegisterResponseDTO register(AuthRequestDTO request) {

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new UserAlreadyExists();
    }

    User user = new User();
    user.setActive(false);
    user.setFullname(request.getFullname());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.getRoles().add(request.getRole());

    User savedUser = userRepository.save(user);

    VerificationCode verificationCode = new VerificationCode();
    verificationCode.setCode(VerificationUtils.generateSixDigitsCode());
    verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(15));
    verificationCode.setUser(savedUser);

    verificationCodeRepository.save(verificationCode);

    String body = """
        Hello %s,

        Welcome to Spotify-Dawood, your activation code is: %d

        Please verify your account within 15 minutes.

        Regards,
        Spotify-Dawood Team.
        """.formatted(savedUser.getFullname(), verificationCode.getCode());

    mqEmailProducer.sendMessage(savedUser.getEmail(), "Activate Your Spotify Profile - Dawood", body);

    return toDTO(savedUser);

  }

  public String login(AuthRequestDTO requestDTO) {

    User user = userRepository.findByEmail(requestDTO.getEmail())
        .orElseThrow(() -> new UserNotFoundException());

    if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Incorrect email or password");
    }

    UserDetails userDetails = customUserDetailsService.loadUserByUsername(requestDTO.getEmail());

    String token = jwtUtils.generateString(userDetails);

    return token;

  }

  private RegisterResponseDTO toDTO(User user) {
    RegisterResponseDTO registerResponseDTO = RegisterResponseDTO.builder()
        .id(user.getId())
        .email(user.getEmail())
        .fullname(user.getFullname())
        .build();

    return registerResponseDTO;
  }

}
