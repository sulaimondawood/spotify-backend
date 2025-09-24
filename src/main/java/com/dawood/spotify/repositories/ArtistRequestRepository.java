package com.dawood.spotify.repositories;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dawood.spotify.entities.ArtistRequest;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.enums.ArtistRequestStatus;

public interface ArtistRequestRepository extends JpaRepository<ArtistRequest, Long> {

  ArtistRequest findByUser(User user);

  boolean existsByUser(User user);

  @Query("""
      SELECT ar FROM ArtistRequest ar
      WHERE ( :status IS NULL OR ar.status = :status)
      AND (:keyword IS NULL
          OR (LOWER(ar.user.fullname) LIKE LOWER(CONCAT('%', :keyword,'%')))
          OR (LOWER(ar.stageName) LIKE LOWER(CONCAT('%',:keyword,'%')))
          )
      AND (:startDate IS NULL OR ar.createdAt >= :startDate)
      AND (:endDate IS NULL OR ar.createdAt <= :endDate)
        """)
  Page<ArtistRequest> getAllArtistWithFilters(
      @Param("status") ArtistRequestStatus status,
      @Param("keyword") String keyword,
      @Param("startDate") LocalDateTime starDate,
      @Param("endDate") LocalDateTime endDate,
      Pageable pageable);

}
