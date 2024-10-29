package com.odaimzabi.dream_shops.services.category;

import com.odaimzabi.dream_shops.models.Category;

import java.util.List;

public interface ICategoryService {
    Category getCategoryById(Long id);
    Category getCategoryByName(String name);
    List<Category> getAllCategories();
    Category updateCategory(Category category,Long id);
    Category  addCategory(Category category);
    void deleteCategoryById(Long id);
}
