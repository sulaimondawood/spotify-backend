package com.dawood.spotify.dtos.playlist;

import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlaylistSongRequest {

  @NotNull(message = "One or more song is required")
  private List<Long> songs;

  @NotNull(message = "You have not selected a playlist")
  private Long playlistId;

}
