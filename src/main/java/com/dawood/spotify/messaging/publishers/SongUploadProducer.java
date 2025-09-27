package com.dawood.spotify.messaging.publishers;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import com.dawood.spotify.dtos.song.UploadSongMessage;
import com.dawood.spotify.messaging.configs.RabbitMqConfig;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SongUploadProducer {

  private final RabbitTemplate rabbitTemplate;

  public void sendMessage(UploadSongMessage message) {

    log.info("Song message producer activated...");

    rabbitTemplate.convertAndSend(RabbitMqConfig.TOPIC_EXCHANGE, RabbitMqConfig.SONG_UPLOAD_ROUTING_KEY, message);
  }

}
