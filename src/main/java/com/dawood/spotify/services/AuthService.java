package com.dawood.spotify.services;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dawood.spotify.Exceptions.InvalidCodeException;
import com.dawood.spotify.Exceptions.UserAlreadyExists;
import com.dawood.spotify.Exceptions.UserException;
import com.dawood.spotify.Exceptions.UserNotFoundException;
import com.dawood.spotify.dtos.auth.AuthRequestDTO;
import com.dawood.spotify.dtos.auth.ForgotPasswordDTO;
import com.dawood.spotify.dtos.auth.RegisterResponseDTO;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.entities.VerificationCode;
import com.dawood.spotify.messaging.publisher.MQEmailProducer;
import com.dawood.spotify.messaging.publisher.MQForgotPasswordProducer;
import com.dawood.spotify.repositories.UserRepository;
import com.dawood.spotify.repositories.VerificationCodeRepository;
import com.dawood.spotify.utils.JwtUtils;
import com.dawood.spotify.utils.VerificationUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtUtils jwtUtils;
  private final CustomUserDetailsService customUserDetailsService;
  private final VerificationCodeRepository verificationCodeRepository;
  private final MQEmailProducer mqEmailProducer;
  private final MQForgotPasswordProducer mqForgotPasswordProducer;

  public RegisterResponseDTO register(AuthRequestDTO request) {

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new UserAlreadyExists();
    }

    User user = new User();
    user.setActive(false);
    user.setFullname(request.getFullname());
    user.setEmail(request.getEmail());
    user.setPassword(passwordEncoder.encode(request.getPassword()));
    user.getRoles().add(request.getRole());

    User savedUser = userRepository.save(user);

    VerificationCode verificationCode = new VerificationCode();
    verificationCode.setCode(VerificationUtils.generateSixDigitsCode());
    verificationCode.setExpiresAt(LocalDateTime.now().plusMinutes(15));
    verificationCode.setUser(savedUser);

    verificationCodeRepository.save(verificationCode);

    String body = """
        Hello %s,

        Welcome to Spotify-Dawood, your activation code is: %d

        Please verify your account within 15 minutes.

        Regards,
        Spotify-Dawood Team.
        """.formatted(savedUser.getFullname(), verificationCode.getCode());

    mqEmailProducer.sendMessage(savedUser.getEmail(), "Activate Your Spotify Profile - Dawood", body);

    return toDTO(savedUser);

  }

  public String login(AuthRequestDTO requestDTO) {

    User user = userRepository.findByEmail(requestDTO.getEmail())
        .orElseThrow(() -> new UserNotFoundException());

    if (!user.isActive()) {
      throw new UserException("Your account is not activated!");
    }

    if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Incorrect email or password");
    }

    UserDetails userDetails = customUserDetailsService.loadUserByUsername(requestDTO.getEmail());

    String token = jwtUtils.generateString(userDetails);

    return token;

  }

  public String activateAccount(int code) {

    VerificationCode verificationCode = verificationCodeRepository.findByCode(code)
        .orElseThrow(() -> new InvalidCodeException("Invalid verification code"));

    if (verificationCode.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new InvalidCodeException("Verification code has expired!");
    }

    User user = verificationCode.getUser();
    user.setActive(true);

    userRepository.save(user);
    verificationCodeRepository.delete(verificationCode);

    return "User account activated!";
  }

  private RegisterResponseDTO toDTO(User user) {
    RegisterResponseDTO registerResponseDTO = RegisterResponseDTO.builder()
        .id(user.getId())
        .email(user.getEmail())
        .fullname(user.getFullname())
        .build();

    return registerResponseDTO;
  }

  private void forgotPassword(ForgotPasswordDTO request) {

    User user = userRepository.findByEmail(request.getEmail())
        .orElseThrow(() -> new UserException("User account not found"));

    int resetCode = VerificationUtils.generateSixDigitsCode();

    verificationCodeRepository.deleteById(user.getId());

    VerificationCode code = new VerificationCode();
    code.setUser(user);
    code.setExpiresAt(LocalDateTime.now().plusMinutes(10));
    code.setCode(resetCode);

    verificationCodeRepository.save(code);

    String[] parts = user.getFullname()
        .trim()
        .split(" ");

    String firstname = parts.length > 0 ? parts[0] : "";

    String body = """

        Hello %s,

        Here is your password reset code: %d

        Kindly ignore if you didn't perform this action.

        Regards,
        Spotify-Dawood Team.
            """.formatted(firstname, resetCode);

    mqForgotPasswordProducer.sendResetPasswordMessage(user.getEmail(), "Password Reset - Spotify-Dawood",
        body);
  }

}
