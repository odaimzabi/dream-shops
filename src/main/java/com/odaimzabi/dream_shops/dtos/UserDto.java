package com.odaimzabi.dream_shops.dtos;
import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private CartDTO cart;
}
