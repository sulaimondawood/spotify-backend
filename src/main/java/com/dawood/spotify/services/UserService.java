package com.dawood.spotify.services;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.dawood.spotify.entities.User;
import com.dawood.spotify.exceptions.user.UserNotFoundException;
import com.dawood.spotify.repositories.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  // @Cacheable(cacheNames = "currentUser", key =
  // "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()")
  public User currentLoggedInUser() {
    String username = SecurityContextHolder.getContext()
        .getAuthentication()
        .getName();

    return userRepository.findByEmail(username)
        .orElseThrow(() -> new UserNotFoundException());

  }

}
