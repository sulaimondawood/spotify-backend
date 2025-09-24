package com.dawood.spotify.services;

import java.time.LocalDateTime;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dawood.spotify.dtos.auth.AuthRequestDTO;
import com.dawood.spotify.dtos.auth.ForgotPasswordDTO;
import com.dawood.spotify.dtos.auth.RegisterResponseDTO;
import com.dawood.spotify.dtos.auth.ResetPasswordDTO;
import com.dawood.spotify.dtos.auth.VerifyCodeDTO;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.entities.VerificationCode;
import com.dawood.spotify.enums.RoleType;
import com.dawood.spotify.exceptions.user.UserAlreadyExists;
import com.dawood.spotify.exceptions.user.UserException;
import com.dawood.spotify.exceptions.user.UserNotFoundException;
import com.dawood.spotify.exceptions.verification.InvalidCodeException;
import com.dawood.spotify.messaging.publishers.MQEmailProducer;
import com.dawood.spotify.messaging.publishers.MQForgotPasswordProducer;
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
    user.getRoles().add(RoleType.USER);

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
        .orElseThrow(() -> new UserNotFoundException("User does not exists"));

    if (!user.isActive()) {
      throw new UserException("Your account is not activated yet!");
    }

    if (!passwordEncoder.matches(requestDTO.getPassword(), user.getPassword())) {
      throw new IllegalArgumentException("Incorrect email or password");
    }

    UserDetails userDetails = customUserDetailsService.loadUserByUsername(requestDTO.getEmail());

    String token = jwtUtils.generateString(userDetails);

    return token;

  }

  public void forgotPassword(ForgotPasswordDTO request) {

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

  public void verifyCode(VerifyCodeDTO codeDTO) {

    User user = userRepository.findByEmail(codeDTO.getEmail())
        .orElseThrow(() -> new UserException("User account not found"));

    VerificationCode code = verificationCodeRepository.findByUserAndCode(user, codeDTO.getCode())
        .orElseThrow(() -> new InvalidCodeException("Invalid verification code"));

    if (code.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new InvalidCodeException("Invalid or expired verification code");
    }

  }

  public void resetPassword(ResetPasswordDTO resetPasswordDTO) {

    User user = userRepository.findByEmail(resetPasswordDTO.getEmail())
        .orElseThrow(() -> new UserException("User account not found"));

    VerificationCode code = verificationCodeRepository.findByUserAndCode(user, resetPasswordDTO.getCode())
        .orElseThrow(() -> new InvalidCodeException("Invalid verification code"));

    if (code.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new InvalidCodeException("Expired verification code");
    }

    if (!resetPasswordDTO.getNewPassword().equals(resetPasswordDTO.getConfirmPassword())) {
      throw new IllegalArgumentException("Password did not match Confirm Password");
    }

    user.setPassword(passwordEncoder.encode(resetPasswordDTO.getNewPassword()));

    userRepository.save(user);

    verificationCodeRepository.delete(code);

  }

  @Transactional
  public String activateAccount(int code) {

    VerificationCode verificationCode = verificationCodeRepository.findByCode(code)
        .orElseThrow(() -> new InvalidCodeException("Invalid verification code"));

    if (verificationCode.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new InvalidCodeException("Verification code has expired!");
    }

    User user = verificationCode.getUser();

    if (user.isActive()) {
      return "Account already activated";
    }

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

}
