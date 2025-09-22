package com.dawood.spotify.messaging.publisher;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import com.dawood.spotify.messaging.configs.RabbitMqConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class MQForgotPasswordProducer {

  private final RabbitTemplate rabbitTemplate;

  public void sendResetPasswordMessage(String to, String subject, String body) {

    Map<String, String> message = Map.of(
        "to", to,
        "subject", subject,
        "body", body);

    log.info("Message sent to exchange");

    rabbitTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE, RabbitMqConfig.RESET_PASSWORD_ROUTING_KEY, message);

  }

}
