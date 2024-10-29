package com.odaimzabi.dream_shops.services.category;

import com.odaimzabi.dream_shops.exceptions.AlreadyExistsException;
import com.odaimzabi.dream_shops.exceptions.ResourceNotFoundException;
import com.odaimzabi.dream_shops.models.Category;
import com.odaimzabi.dream_shops.repositories.CategoryRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements ICategoryService {
  private final CategoryRepository categoryRepository;

  @Override
  public Category getCategoryById(Long id) {
    return categoryRepository
        .findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
  }

  @Override
  public Category getCategoryByName(String name) {
    return categoryRepository.findByName(name);
  }

  @Override
  public List<Category> getAllCategories() {
    return null;
  }

  @Override
  public Category updateCategory(Category category, Long id) {
    return Optional.ofNullable(this.getCategoryById(id))
        .map(
            oldCategory -> {
              oldCategory.setName(category.getName());
              return categoryRepository.save(oldCategory);
            })
        .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
  }

  @Override
  public Category addCategory(Category category) {
    return Optional.of(category)
        .filter(c -> !categoryRepository.existsByName(c.getName()))
        .map(categoryRepository::save)
        .orElseThrow(() -> new AlreadyExistsException("Category already exists by this name!"));
  }

  @Override
  public void deleteCategoryById(Long id) {
    categoryRepository
        .findById(id)
        .ifPresentOrElse(
            categoryRepository::delete, () -> new ResourceNotFoundException("Category not found!"));
  }
}
