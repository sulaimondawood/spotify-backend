package com.dawood.spotify.dtos.user;

import java.util.ArrayList;
import java.util.List;

import com.dawood.spotify.entities.ArtistProfile;
import com.dawood.spotify.entities.Audit;
import com.dawood.spotify.enums.RoleType;

import lombok.Data;

@Data
public class UserResponseDTO {
  private Long id;

  private String username;

  private String fullname;

  private String email;

  private String password;

  private String photoUrl;

  private String coverPhotoUrl;

  private List<RoleType> roles = new ArrayList<>();

  private boolean active;

  private ArtistProfile artistProfile;

  private Audit audit = new Audit();

}
