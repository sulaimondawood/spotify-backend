package com.dawood.spotify.entities;

import com.dawood.spotify.enums.RoleType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roles")
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class UserRole {

  @Id
  @GeneratedValue
  private Long id;

  @Enumerated(EnumType.STRING)
  private RoleType name;

}
