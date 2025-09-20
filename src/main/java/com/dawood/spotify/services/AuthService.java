package com.dawood.spotify.services;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dawood.spotify.Exceptions.UserAlreadyExists;
import com.dawood.spotify.Exceptions.UserNotFoundException;
import com.dawood.spotify.dtos.auth.AuthRequestDTO;
import com.dawood.spotify.dtos.auth.RegisterResponseDTO;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.repositories.UserRepository;
import com.dawood.spotify.utils.JwtUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtils jwtUtils;
  private final CustomUserDetailsService customUserDetailsService;

  public RegisterResponseDTO register(AuthRequestDTO request) {

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new UserAlreadyExists();
    }

    User user = new User();

    user.setActive(false);
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.getRoles().add(request.getRole());

    User savedUser = userRepository.save(user);

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
        .photoUrl(user.getPhotoUrl())
        .username(user.getUsername())
        .build();

    return registerResponseDTO;
  }

}
