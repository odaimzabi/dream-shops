package com.odaimzabi.dream_shops.controllers;

import com.odaimzabi.dream_shops.dtos.ImageDTO;
import com.odaimzabi.dream_shops.exceptions.ResourceNotFoundException;
import com.odaimzabi.dream_shops.models.Image;
import com.odaimzabi.dream_shops.responses.ApiResponse;
import com.odaimzabi.dream_shops.services.image.ImageServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.SQLException;
import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "${api.prefix}/images")
public class ImageController {
  private final ImageServiceImpl imageService;

  @PostMapping("/upload")
  public ResponseEntity<ApiResponse> saveImages(
      @RequestParam List<MultipartFile> files, @RequestParam Long productId) {
    try {
      List<ImageDTO> imagesDTOS = imageService.saveImages(files, productId);
      return ResponseEntity.ok(new ApiResponse("Upload successful", imagesDTOS));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Upload failed", e.getMessage()));
    }
  }

  @GetMapping("/image/download/{imageId}")
  public ResponseEntity<Resource> downloadImage(@PathVariable Long imageId) throws SQLException {
    Image image = imageService.getImageById(imageId);
    ByteArrayResource resource =
        new ByteArrayResource(image.getImage().getBytes(1, (int) image.getImage().length()));

    return ResponseEntity.ok()
        .contentType(MediaType.parseMediaType(image.getFileType()))
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + image.getFileName())
        .body(resource);
  }

  @PutMapping("/image/{imageId}/update")
  public ResponseEntity<ApiResponse> updateImage(
      @PathVariable Long imageId, @RequestBody MultipartFile file) {
    try {
      Image image = imageService.getImageById(imageId);
      if (image != null) {
        imageService.updateImage(file, imageId);
        return ResponseEntity.ok().body(new ApiResponse("Upload success", null));
      }

    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }

    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(new ApiResponse("Upload failed", null));
  }

  @DeleteMapping("/image/{imageId}/delete")
  public ResponseEntity<ApiResponse> deleteImage(@PathVariable Long imageId) {
    try {
      Image image = imageService.getImageById(imageId);
      if (image != null) {
        imageService.deleteByImageId(imageId);
        return ResponseEntity.ok().body(new ApiResponse("Delete success", null));
      }

    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
    }

    return ResponseEntity.status(INTERNAL_SERVER_ERROR)
        .body(new ApiResponse("Delete failed", null));
  }
}
