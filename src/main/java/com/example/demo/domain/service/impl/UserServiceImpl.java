package com.example.demo.domain.service.impl;

import com.example.demo.api.dto.UserCreateRequest;
import com.example.demo.api.dto.UserResponse;
import com.example.demo.api.mapper.UserMapper;
import com.example.demo.domain.repository.UserRepository;
import com.example.demo.domain.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);

    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Override
    public UserResponse create(UserCreateRequest request) {
        log.info("Creating user with email={}", request.getEmail());
        var user = UserMapper.toEntity(request, encoder.encode(request.getPassword()));
        return UserMapper.toResponse(repository.save(user));
    }

    @Override
    @Cacheable(value = "users", key = "#id")
    public UserResponse getUserById(Long id) {
        log.info("Fetching user from DB with id={}", id);
        var user = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return UserMapper.toResponse(user);
    }

    @Override
    @CacheEvict(value = "users", key = "#id")  // remove from cache automatically
    public void deleteUser(Long id) {
        log.info("Deleting user with id={}", id);
        repository.deleteById(id);
    }
}

