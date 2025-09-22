package com.dawood.spotify.messaging.consumers;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import com.dawood.spotify.messaging.configs.RabbitMqConfig;
import com.dawood.spotify.services.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MQForgotPasswordConsumer {
  private final EmailService emailService;

  @RabbitListener(queues = RabbitMqConfig.RESET_PASSWORD_QUEUE)
  public void consumeResetPasswordMessage(Map<String, String> message) {

    String to = message.get("to");
    String subject = message.get("subject");
    String body = message.get("body");

    log.info("Message consumed: {}", to);

    emailService.sendSimpleMail(to, subject, body);

  }

}
