package com.dawood.spotify.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.dawood.spotify.entities.User;
import com.dawood.spotify.exceptions.user.UserNotFoundException;
import com.dawood.spotify.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public User currentLoggedInUser() {
    String username = SecurityContextHolder.getContext()
        .getAuthentication()
        .getName();

    return userRepository.findByEmail(username)
        .orElseThrow(() -> new UserNotFoundException());

  }

}
