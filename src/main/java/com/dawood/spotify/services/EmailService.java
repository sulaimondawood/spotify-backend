package com.dawood.spotify.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

  private final JavaMailSender javaMailSender;

  @Value("${spring.application.name}")
  private final String sender;

  public void sendSimpleMail(String to, String subject, String body) {

    try {

      SimpleMailMessage message = new SimpleMailMessage();

      String capitalizedSender = sender.substring(0, 1).toUpperCase() + sender.substring(1).toLowerCase();

      message.setFrom(capitalizedSender);
      message.setTo(to);
      message.setSubject(subject);
      message.setText(body);

      javaMailSender.send(message);
    } catch (Exception e) {
      log.error(e.getMessage());
    }

  }

  public void sendHtmlMail(String to, String subject, String body) {

    try {

      MimeMessage message = javaMailSender.createMimeMessage();

      MimeMessageHelper helper = new MimeMessageHelper(message);

      helper.setFrom(sender);
      helper.setTo(to);
      helper.setSubject(subject);
      helper.setSentDate(new Date());
      helper.setText(body, true);

      javaMailSender.send(message);

    } catch (MailSendException e) {
      log.error(e.getMessage());
    } catch (MailException e) {
      log.error(e.getMessage());
    } catch (Exception e) {
      log.error("Unable to send mail", e.getMessage());
    }
  }

}
