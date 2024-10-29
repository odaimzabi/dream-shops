package com.odaimzabi.dream_shops.requests;

import com.odaimzabi.dream_shops.models.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductRequest {
  private Long id;
  private String name;
  private String description;
  private String brand;
  private BigDecimal price;
  private int inventory;
  private Category category;
}
