package com.odaimzabi.dream_shops.repositories;

import com.odaimzabi.dream_shops.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByName(String name);

    boolean existsByName(String name);
}
