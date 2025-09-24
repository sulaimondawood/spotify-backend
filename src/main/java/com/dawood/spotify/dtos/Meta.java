package com.dawood.spotify.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Meta {

  private int pageNo;

  private int pageSize;

  private long totalPages;

  private boolean hasNext;

  private boolean hasPrev;

}
