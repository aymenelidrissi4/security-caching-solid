package com.example.demo.domain.service;

import com.example.demo.api.dto.UserCreateRequest;
import com.example.demo.api.dto.UserResponse;

public interface UserService {
    UserResponse create(UserCreateRequest request);
    UserResponse getUserById(Long id);
    void deleteUser(Long id);  // new method
}
