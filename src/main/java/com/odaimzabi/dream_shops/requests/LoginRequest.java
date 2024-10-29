package com.odaimzabi.dream_shops.requests;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}
