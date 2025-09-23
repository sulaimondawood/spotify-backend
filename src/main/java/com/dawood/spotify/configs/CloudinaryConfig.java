package com.dawood.spotify.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

@Configuration
public class CloudinaryConfig {

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
