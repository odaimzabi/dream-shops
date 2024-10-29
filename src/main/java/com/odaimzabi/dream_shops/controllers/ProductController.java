package com.odaimzabi.dream_shops.controllers;

import com.odaimzabi.dream_shops.dtos.ProductDTO;
import com.odaimzabi.dream_shops.exceptions.AlreadyExistsException;
import com.odaimzabi.dream_shops.exceptions.ResourceNotFoundException;
import com.odaimzabi.dream_shops.models.Product;
import com.odaimzabi.dream_shops.requests.AddProductRequest;
import com.odaimzabi.dream_shops.requests.UpdateProductRequest;
import com.odaimzabi.dream_shops.responses.ApiResponse;
import com.odaimzabi.dream_shops.services.product.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequestMapping(value = "${api.prefix}/products")
@RequiredArgsConstructor
public class ProductController {
  private final ProductServiceImpl productService;

  @GetMapping("/")
  public ResponseEntity<ApiResponse> getAllProducts() {
    try {
      List<Product> products = productService.getAllProducts();
      List<ProductDTO> productDTOS = productService.getConvertedProducts(products);
      return ResponseEntity.ok().body(new ApiResponse("success", productDTOS));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Error:", e.getMessage()));
    }
  }

  @GetMapping("/{productId}")
  public ResponseEntity<ApiResponse> getProductById(@PathVariable Long productId) {
    try {
      Product product = productService.getProductById(productId);
      ProductDTO productDTO = productService.convertToDto(product);
      return ResponseEntity.ok().body(new ApiResponse("success", productDTO));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Error:", e.getMessage()));
    }
  }

  @DeleteMapping("/{productId}")
  public ResponseEntity<ApiResponse> deleteProductById(@PathVariable Long productId) {
    try {
      productService.deleteProductById(productId);
      return ResponseEntity.ok().body(new ApiResponse("Product was deleted successfully", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Error:", e.getMessage()));
    }
  }

  @PutMapping("/{productId}")
  public ResponseEntity<ApiResponse> updateProductById(
      @RequestBody UpdateProductRequest productRequest, @PathVariable Long productId) {
    try {
      Product updatedProduct = productService.updateProduct(productRequest, productId);
      ProductDTO updatedProductDto = productService.convertToDto(updatedProduct);
      return ResponseEntity.ok()
          .body(new ApiResponse("Product was updated successfully", updatedProductDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Error:", e.getMessage()));
    }
  }
  @PreAuthorize("")
  @PostMapping("/add")
  public ResponseEntity<ApiResponse> addProduct(@RequestBody AddProductRequest productRequest) {
    try {
      Product addedProduct = productService.addProduct(productRequest);
      return ResponseEntity.ok().body(new ApiResponse("Product added", addedProduct));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(CONFLICT).body(new ApiResponse("Product already exists!", null));
    }
  }
}
