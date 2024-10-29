package com.odaimzabi.dream_shops.services.user;

import com.odaimzabi.dream_shops.dtos.UserDto;
import com.odaimzabi.dream_shops.models.User;
import com.odaimzabi.dream_shops.requests.CreateUserRequest;
import com.odaimzabi.dream_shops.requests.UpdateUserRequest;

public interface IUserService {
    User getUserById(Long userId);
    User createUser(CreateUserRequest request);
    User updateUser(UpdateUserRequest request, Long userId);
    void deleteUser(Long userId);

    UserDto convertUserToDto(User user);

    User getAuthenticatedUser();
}
