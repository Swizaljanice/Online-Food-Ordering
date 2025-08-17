package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.controller.AuthController;
import com.online.foodOrderingSystem.dto.RestaurantUserRequestDTO;
import com.online.foodOrderingSystem.dto.RestaurantUserResponseDTO;
import com.online.foodOrderingSystem.entity.RestaurantUser;
import com.online.foodOrderingSystem.repository.RestaurantUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class RestaurantUserService {

    @Autowired
    private RestaurantUserRepository userRepository;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public RestaurantUserResponseDTO registerUser(RestaurantUserRequestDTO request) {
        log.info("Attempting to register user: email={}, role={}, name={}",
                request.getEmail(), request.getRole(), request.getName());

        if (userRepository.existsByEmail(request.getEmail())) {
            log.warn("Registration failed: Email {} is already in use", request.getEmail());
            throw new RuntimeException("Email already in use");
        }

        RestaurantUser user = RestaurantUser.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(RestaurantUser.Role.valueOf(request.getRole().toUpperCase()))
                .phone(request.getPhone())
                .createdAt(LocalDateTime.now())
                .build();

        RestaurantUser savedUser = userRepository.save(user);
        log.info("User registered successfully: id={}, email={}", savedUser.getId(), savedUser.getEmail());

        return mapToDTO(savedUser);
    }

    public List<RestaurantUserResponseDTO> getAllUsers() {
        log.info("Fetching all users");
        List<RestaurantUserResponseDTO> users = userRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
        log.info("Found {} registered users", users.size());
        return users;
    }

    private RestaurantUserResponseDTO mapToDTO(RestaurantUser restaurantUser) {
        return RestaurantUserResponseDTO.builder()
                .id(restaurantUser.getId())
                .name(restaurantUser.getName())
                .email(restaurantUser.getEmail())
                .role(restaurantUser.getRole().name())
                .phone(restaurantUser.getPhone())
                .createdAt(restaurantUser.getCreatedAt() != null
                        ? restaurantUser.getCreatedAt().toString()
                        : null)
                .build();
    }
}
