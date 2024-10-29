package com.odaimzabi.dream_shops.security.user;

import com.odaimzabi.dream_shops.models.User;
import com.odaimzabi.dream_shops.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ShopUserDetailsService implements UserDetailsService {
  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    User user =
        Optional.ofNullable(userRepository.findByEmail(email))
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));

    return ShopUserDetails.buildUserDetails(user);
  }
}
