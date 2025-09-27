package com.dawood.spotify.services;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CloudinaryService {

  private final Cloudinary cloudinary;

  public Map<String, Object> uploadMultipart(MultipartFile file, String name) throws IOException {
    try {
      return cloudinary.uploader()
          .upload(file.getBytes(), ObjectUtils.asMap(
              "use_filename", true,
              "unique_filename", true,
              "folder", "users"));

    } catch (Exception e) {
      log.error("Error uploading file {}", e.getMessage());
      throw e;
    }
  }

  public Map<String, Object> uploadMultipart(File file, String name) throws IOException {
    try {
      return cloudinary.uploader()
          .upload(file, ObjectUtils.asMap(
              "use_filename", true,
              "unique_filename", true,
              "folder", "users"));

    } catch (Exception e) {
      log.error("Error uploading file {}", e.getMessage());
      throw e;
    }
  }

  public Map<String, Object> uploadMultipart(MultipartFile file, String name, String resourceType) throws IOException {
    try {
      return cloudinary.uploader()
          .upload(file.getBytes(), ObjectUtils.asMap(
              "resource_type", resourceType,
              "use_filename", true,
              "unique_filename", true,
              "folder", "users"));

    } catch (Exception e) {
      log.error("Error uploading file {}", e.getMessage());
      throw e;
    }
  }

  public Map<String, Object> uploadMultipart(File file, String name, String resourceType) throws IOException {
    try {
      return cloudinary.uploader()
          .upload(file, ObjectUtils.asMap(
              "resource_type", resourceType,
              "use_filename", true,
              "unique_filename", true,
              "folder", "users"));

    } catch (Exception e) {
      log.error("Error uploading file {}", e.getMessage());
      throw e;
    }
  }

}
