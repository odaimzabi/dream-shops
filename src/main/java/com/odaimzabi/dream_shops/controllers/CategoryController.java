package com.odaimzabi.dream_shops.controllers;

import com.odaimzabi.dream_shops.exceptions.AlreadyExistsException;
import com.odaimzabi.dream_shops.exceptions.ResourceNotFoundException;
import com.odaimzabi.dream_shops.models.Category;
import com.odaimzabi.dream_shops.responses.ApiResponse;
import com.odaimzabi.dream_shops.services.category.CategoryServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "${api.prefix}/categories")
public class CategoryController {
  private final CategoryServiceImpl categoryService;

  @GetMapping("/")
  public ResponseEntity<ApiResponse> getAllCategories() {
    try {
      List<Category> categories = categoryService.getAllCategories();
      return ResponseEntity.ok().body(new ApiResponse("success", categories));
    } catch (Exception e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Error:", e.getMessage()));
    }
  }

  @GetMapping("/{categoryId}")
  public ResponseEntity<ApiResponse> getCategoryById(@PathVariable Long categoryId) {
    try {
      Category category = categoryService.getCategoryById(categoryId);
      return ResponseEntity.ok().body(new ApiResponse("success", category));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Error:", e.getMessage()));
    }
  }

  @DeleteMapping("/{categoryId}")
  public ResponseEntity<ApiResponse> deleteCategoryById(@PathVariable Long categoryId) {
    try {
      categoryService.deleteCategoryById(categoryId);
      return ResponseEntity.ok().body(new ApiResponse("Category was deleted successfully", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Error:", e.getMessage()));
    }
  }

  @PutMapping("/{categoryId}")
  public ResponseEntity<ApiResponse> updateCategoryById(
      @RequestBody Category category, @PathVariable Long categoryId) {
    try {
      Category updatedCategory = categoryService.updateCategory(category, categoryId);
      return ResponseEntity.ok()
          .body(new ApiResponse("Category was updated successfully", updatedCategory));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(INTERNAL_SERVER_ERROR)
          .body(new ApiResponse("Error:", e.getMessage()));
    }
  }

  @PostMapping("/add")
  @ResponseBody
  public ResponseEntity<ApiResponse> addCategory(@RequestBody Category category) {
    try {
      Category addedCategory = categoryService.addCategory(category);
      return ResponseEntity.ok().body(new ApiResponse("Category added", addedCategory));
    } catch (AlreadyExistsException e) {
      return ResponseEntity.status(CONFLICT)
          .body(new ApiResponse("Category already exists!", null));
    }
  }
}
