package com.dawood.spotify.initializers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.dawood.spotify.entities.User;
import com.dawood.spotify.enums.RoleType;
import com.dawood.spotify.repositories.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class AdminSeeder implements CommandLineRunner {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;

  @Value("${app.seeder.super-admin-password}")
  private String superAdminPassword;

  @Value("${app.seeder.super-admin-email}")
  private String superAdminEmail;

  @Override
  public void run(String... args) throws Exception {

    boolean superAdminExists = userRepository.findByEmail(superAdminEmail).isPresent();

    if (!superAdminExists) {
      User superAdmin = new User();
      superAdmin.setActive(true);
      superAdmin.setEmail(superAdminEmail);
      superAdmin.setFullname("Dawood Spotify");
      superAdmin.getRoles().add(RoleType.SUPER_ADMIN);
      superAdmin.setPassword(passwordEncoder.encode(superAdminPassword));

      userRepository.save(superAdmin);

      log.info(superAdmin.getEmail());
    }

  }

}
