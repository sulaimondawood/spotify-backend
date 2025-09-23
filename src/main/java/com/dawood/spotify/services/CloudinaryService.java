package com.dawood.spotify.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Service
public class CloudinaryService {

  @Value("${app.cloudinary.api-name}")
  private String cloudApiName;

  @Value("${app.cloudinary.api-secret}")
  private String cloudApiSecret;

  @Value("${app.cloudinary.api-key}")
  private String cloudApiKey;

  @Bean
  public Cloudinary cloudinary() {
    return new Cloudinary(ObjectUtils.asMap(
        "cloud_name", cloudApiName,
        "api_key", cloudApiKey,
        "api_secret", cloudApiSecret,
        "secure", true));
  }

}
