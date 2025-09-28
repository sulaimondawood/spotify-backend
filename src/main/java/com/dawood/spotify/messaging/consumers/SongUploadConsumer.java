package com.dawood.spotify.messaging.consumers;

import java.io.File;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.dawood.spotify.dtos.song.UploadSongMessage;
import com.dawood.spotify.entities.Song;
import com.dawood.spotify.entities.SongUploadJob;
import com.dawood.spotify.entities.User;
import com.dawood.spotify.enums.UploadStatus;
import com.dawood.spotify.exceptions.song.SongNotFoundException;
import com.dawood.spotify.exceptions.user.UserNotFoundException;
import com.dawood.spotify.messaging.configs.RabbitMqConfig;
import com.dawood.spotify.repositories.SongRepository;
import com.dawood.spotify.repositories.SongUploadJobRepository;
import com.dawood.spotify.repositories.UserRepository;
import com.dawood.spotify.services.CloudinaryService;
import com.dawood.spotify.services.UserService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class SongUploadConsumer {

  private final CloudinaryService cloudinaryService;
  private final SongUploadJobRepository songUploadJobRepository;
  private final SongRepository songRepository;
  private final UserRepository userRepository;

  @RabbitListener(queues = RabbitMqConfig.SONG_UPLOAD_QUEUE_NAME)
  public void consumeMessage(UploadSongMessage message) {
    log.info("Processing upload job {}", message.getUploadId());

    SongUploadJob songUploadJob = songUploadJobRepository.findById(message.getUploadId())
        .orElseThrow(() -> new SongNotFoundException());

    User user = userRepository.findById(message.getUserId())
        .orElseThrow(() -> new UserNotFoundException());

    try {

      File audioFile = new File(message.getAudioFilePath());
      File coverArtFile = new File(message.getCoverArtFilePath());

      var audioFuture = CompletableFuture.supplyAsync(() -> {
        try {
          return cloudinaryService.uploadMultipart(audioFile, message.getSongName(), "auto");
        } catch (Exception e) {
          throw new CompletionException(e);
        }
      });

      var coverFuture = CompletableFuture.supplyAsync(() -> {
        try {
          return cloudinaryService.uploadMultipart(coverArtFile, message.getSongName() + "_cover");
        } catch (Exception e) {
          throw new CompletionException(e);
        }
      });

      Map<String, Object> coverImageData = coverFuture.join();
      Map<String, Object> audioData = audioFuture.join();

      Song song = new Song();
      song.setArtistProfile(user.getArtistProfile());
      song.setAudioUrl(audioData.get("secure_url").toString());
      song.setCoverArtUrl(coverImageData.get("secure_url").toString());
      song.setReleaseDate(message.getReleaseDate());
      song.setGenre(message.getGenre());
      song.setDuration((Double) audioData.get("duration"));
      song.setPlayCount(0);
      song.setName(message.getSongName());

      songRepository.save(song);

      songUploadJob.setStatus(UploadStatus.COMPLETED);
      songUploadJob.setMessage("Upload completed successfully");
      songUploadJob.setSong(song);

    } catch (Exception e) {
      songUploadJob.setStatus(UploadStatus.FAILED);
      songUploadJob.setMessage("Upload failed");
      log.error("Upload job {} failed", message.getUploadId(), e);
    }

    songUploadJobRepository.save(songUploadJob);

    new File(message.getAudioFilePath()).delete();
    new File(message.getCoverArtFilePath()).delete();

  }

}
