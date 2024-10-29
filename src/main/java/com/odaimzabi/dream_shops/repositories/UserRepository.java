package com.odaimzabi.dream_shops.repositories;

import com.odaimzabi.dream_shops.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
    User findByEmail(String email);
}
