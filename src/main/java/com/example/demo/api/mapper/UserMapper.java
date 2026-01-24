package com.example.demo.api.mapper;

import com.example.demo.api.dto.UserCreateRequest;
import com.example.demo.api.dto.UserResponse;
import com.example.demo.domain.model.User;

public final class UserMapper {

    private UserMapper() {
    }

    public static User toEntity(UserCreateRequest dto, String encodedPassword) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(encodedPassword);
        user.setRole("ROLE_USER");
        return user;
    }

    public static UserResponse toResponse(User user) {
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        return dto;
    }
}