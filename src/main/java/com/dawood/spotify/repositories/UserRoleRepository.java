package com.dawood.spotify.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dawood.spotify.entities.UserRole;
import com.dawood.spotify.enums.RoleType;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

  Optional<UserRole> findByName(RoleType name);

}
