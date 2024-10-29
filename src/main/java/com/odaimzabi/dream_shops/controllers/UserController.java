package com.odaimzabi.dream_shops.controllers;

import com.odaimzabi.dream_shops.dtos.UserDto;
import com.odaimzabi.dream_shops.exceptions.ResourceNotFoundException;
import com.odaimzabi.dream_shops.models.User;
import com.odaimzabi.dream_shops.requests.CreateUserRequest;
import com.odaimzabi.dream_shops.requests.UpdateUserRequest;
import com.odaimzabi.dream_shops.responses.ApiResponse;
import com.odaimzabi.dream_shops.services.user.UserServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestController
@RequestMapping(value = "${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
  private final UserServiceImpl userService;

  @GetMapping("/{userId}")
  public ResponseEntity<ApiResponse> getUserById(@PathVariable Long userId) {
    try {
      User user = userService.getUserById(userId);
      UserDto userDto = userService.convertUserToDto(user);
      return ResponseEntity.ok().body(new ApiResponse("Success", userDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error:", e.getMessage()));
    }
  }

  @PostMapping("/add")
  public ResponseEntity<ApiResponse> createUser(@Valid @RequestBody CreateUserRequest request) {
    try {
      User user = userService.createUser(request);
      UserDto userDto = userService.convertUserToDto(user);
      return ResponseEntity.ok().body(new ApiResponse("Success", userDto));
    } catch (RuntimeException e) {
      return ResponseEntity.status(CONFLICT).body(new ApiResponse("Error:", e.getMessage()));
    }
  }

  @PutMapping("/{userId}/update")
  public ResponseEntity<ApiResponse> updateUser(
      @RequestBody UpdateUserRequest request, @PathVariable Long userId) {
    try {
      User user = userService.updateUser(request, userId);
      UserDto userDto = userService.convertUserToDto(user);
      return ResponseEntity.ok().body(new ApiResponse("Success", userDto));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error:", e.getMessage()));
    }
  }

  @DeleteMapping("/{userId}/delete")
  public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
    try {
      userService.deleteUser(userId);
      return ResponseEntity.ok().body(new ApiResponse("User has been deleted", null));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.status(NOT_FOUND).body(new ApiResponse("Error:", e.getMessage()));
    }
  }
}
