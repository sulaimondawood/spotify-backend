package com.dawood.spotify.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dawood.spotify.Exceptions.UserAlreadyExists;
import com.dawood.spotify.dtos.auth.AuthRequestDTO;
import com.dawood.spotify.dtos.auth.RegisterResponseDTO;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.entities.UserRole;
import com.dawood.spotify.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public RegisterResponseDTO register(AuthRequestDTO request) {

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new UserAlreadyExists();
    }

    User user = new User();

    UserRole role = new UserRole();
    role.setName(request.getRole());

    user.setActive(false);
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.getRoles().add(role);

    User savedUser = userRepository.save(user);

    return toDTO(savedUser);

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
