package com.odaimzabi.dream_shops.services.user;

import com.odaimzabi.dream_shops.dtos.UserDto;
import com.odaimzabi.dream_shops.exceptions.ResourceNotFoundException;
import com.odaimzabi.dream_shops.models.User;
import com.odaimzabi.dream_shops.repositories.UserRepository;
import com.odaimzabi.dream_shops.requests.CreateUserRequest;
import com.odaimzabi.dream_shops.requests.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
  private final UserRepository userRepository;
  private final ModelMapper modelMapper;
  private final PasswordEncoder passwordEncoder;

  @Override
  public User getUserById(Long userId) {
    return userRepository
        .findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  @Override
  public User createUser(CreateUserRequest request) {

    request.setPassword(new BCryptPasswordEncoder().encode(request.getPassword()));
    return userRepository.save(new User(request));
  }

  @Override
  public User updateUser(UpdateUserRequest request, Long userId) {
    return userRepository
        .findById(userId)
        .map(
            existingUser -> {
              existingUser.setLastName(request.getLastName());
              existingUser.setFirstName(request.getFirstName());
              return userRepository.save(existingUser);
            })
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));
  }

  @Override
  public void deleteUser(Long userId) {
    userRepository
        .findById(userId)
        .ifPresentOrElse(
            userRepository::delete, () -> new ResourceNotFoundException("User not found"));
  }

  @Override
  public UserDto convertUserToDto(User user) {
    return modelMapper.map(user, UserDto.class);
  }

  @Override
  public User getAuthenticatedUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    String email = auth.getName();

    return this.userRepository.findByEmail(email);
  }
}
