package com.odaimzabi.dream_shops.repositories;

import com.odaimzabi.dream_shops.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image,Long> {
    List<Image> findByProductId(Long productId);
}
