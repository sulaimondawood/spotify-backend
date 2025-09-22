package com.dawood.spotify.messaging.publishers;

import java.util.Map;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dawood.spotify.messaging.configs.RabbitMqConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MQEmailProducer {

  private final RabbitTemplate rabbitTemplate;

  public void sendMessage(String to, String subject, String body) {

    Map<String, String> message = Map.of(
        "to", to,
        "subject", subject,
        "body", body);

    log.info("Message sent");

    rabbitTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE, "routing.key", message);
  }

}
