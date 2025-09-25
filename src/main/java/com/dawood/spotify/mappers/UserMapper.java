package com.dawood.spotify.mappers;

import com.dawood.spotify.dtos.user.UserResponseDTO;
import com.dawood.spotify.entities.User;

public class UserMapper {

  public UserResponseDTO tDTO(User user) {

    UserResponseDTO responseDTO = new UserResponseDTO();
    responseDTO.setId(user.getId());
    responseDTO.setUsername(user.getUsername());
    responseDTO.setFullname(user.getFullname());
    responseDTO.setEmail(user.getEmail());
    responseDTO.setCoverPhotoUrl(user.getCoverPhotoUrl());
    responseDTO.setRoles(user.getRoles());
    responseDTO.setActive(user.isActive());

    return responseDTO;
  }

}
