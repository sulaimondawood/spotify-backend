package com.dawood.spotify.messaging.consumer;

import java.util.Map;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dawood.spotify.messaging.configs.RabbitMqConfig;
import com.dawood.spotify.services.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class MQEmailConsumer {

  private final EmailService emailService;

  @RabbitListener(queues = RabbitMqConfig.QUEUE_NAME)
  public void consume(Map<String, String> message) {

    log.info("Message consumed");
    emailService.sendSimpleMail(message.get("to"), message.get("subject"), message.get("body"));
  }

}
