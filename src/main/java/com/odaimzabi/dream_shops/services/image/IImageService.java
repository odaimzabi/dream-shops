package com.odaimzabi.dream_shops.services.image;

import com.odaimzabi.dream_shops.dtos.ImageDTO;
import com.odaimzabi.dream_shops.models.Image;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IImageService {
    Image getImageById(Long id);
    void deleteByImageId(Long id);
    List<ImageDTO> saveImages(List<MultipartFile> files, Long productId);
    void updateImage(MultipartFile file, Long id);
}
