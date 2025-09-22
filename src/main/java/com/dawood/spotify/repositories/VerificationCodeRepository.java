package com.dawood.spotify.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dawood.spotify.entities.User;
import com.dawood.spotify.entities.VerificationCode;

@Repository
public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {

  Optional<VerificationCode> findByCode(int code);

  Optional<VerificationCode> findByUserAndCode(User user, int code);

}
