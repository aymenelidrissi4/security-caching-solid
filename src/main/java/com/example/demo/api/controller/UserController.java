package com.example.demo.api.controller;


import com.example.demo.api.dto.UserCreateRequest;
import com.example.demo.api.dto.UserResponse;
import com.example.demo.domain.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
public class UserController {


    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@Valid @RequestBody UserCreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.create(request));
    }

    @GetMapping("/secure")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> securedEndpoint() {
        return ResponseEntity.ok("You are authenticated");
    }
}