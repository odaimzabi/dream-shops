package com.odaimzabi.dream_shops.services.image;

import com.odaimzabi.dream_shops.dtos.ImageDTO;
import com.odaimzabi.dream_shops.exceptions.ResourceNotFoundException;
import com.odaimzabi.dream_shops.models.Image;
import com.odaimzabi.dream_shops.models.Product;
import com.odaimzabi.dream_shops.repositories.ImageRepository;
import com.odaimzabi.dream_shops.services.product.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageServiceImpl implements IImageService {
  private final ImageRepository imageRepository;
  private final ProductServiceImpl productService;

  @Override
  public Image getImageById(Long id) {
    return imageRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Image does not exist!"));
  }

  @Override
  public void deleteByImageId(Long id) {
    imageRepository
        .findById(id)
        .ifPresentOrElse(
            imageRepository::delete, () -> new ResourceNotFoundException("Image not found"));
  }

  @Override
  public List<ImageDTO> saveImages(List<MultipartFile> files, Long productId) {
    Product product = productService.getProductById(productId);
    List<ImageDTO> imageDTOS = new ArrayList<>();

    for (MultipartFile file : files) {
      try {
        Image image = new Image();
        image.setFileName(file.getOriginalFilename());
        image.setFileType(file.getContentType());
        image.setImage(new SerialBlob(file.getBytes()));

        String downloadUrl = "/api/v1/images/image/download";
        image.setDownloadUrl(downloadUrl + image.getId());

        Image savedImage = imageRepository.save(image);
        savedImage.setDownloadUrl(downloadUrl + savedImage.getId());
        imageRepository.save(savedImage);

        ImageDTO imageDTO = new ImageDTO();
        imageDTO.setFileName(savedImage.getFileName());
        imageDTO.setId(savedImage.getId());
        imageDTO.setDownloadUrl(savedImage.getDownloadUrl());

        imageDTOS.add(imageDTO);

      } catch (IOException | SQLException e) {
        throw new RuntimeException(e.getMessage());
      }
    }

    return imageDTOS;
  }

  @Override
  public void updateImage(MultipartFile file, Long id) {
    Image image = this.getImageById(id);

    try {
      image.setFileName(file.getOriginalFilename());
      image.setImage(new SerialBlob(file.getBytes()));
    } catch (IOException | SQLException e) {
      throw new RuntimeException(e.getMessage());
    }
  }
}
