package com.odaimzabi.dream_shops.models;

import com.odaimzabi.dream_shops.requests.CreateUserRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String firstName;
  private String lastName;

  @Column(name = "email", unique = true)
  private String email;

  @Column(columnDefinition = "varchar2(20)")
  private String password;

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
  private Cart cart;

  public User(CreateUserRequest request) {
    this.firstName = request.getFirstName();
    this.lastName = request.getLastName();
    this.email = request.getEmail();
    this.password = request.getPassword();
  }
}
