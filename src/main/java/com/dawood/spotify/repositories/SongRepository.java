package com.dawood.spotify.repositories;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.dawood.spotify.entities.Song;
import com.dawood.spotify.entities.User;

public interface SongRepository extends JpaRepository<Song, Long> {

  @Query("""
      SELECT s FROM Song s
      WHERE (:keyword IS NULL OR LOWER(s.name) LIKE LOWER(CONCAT('%', :keyword,'%')))
      AND (:startDate IS NULL OR s.createdAt>= :startDate)
      AND (:endDate IS NULL OR s.createdAt <= :endDate)

        """)
  Page<Song> getAllSongsWithFilters(
      @Param("keyword") String keyword,
      @Param("startDate") LocalDateTime startDate,
      @Param("endDate") LocalDateTime endDate,
      Pageable pageable);

  Optional<Song> findByIdAndUser(Long id, User user);

}
