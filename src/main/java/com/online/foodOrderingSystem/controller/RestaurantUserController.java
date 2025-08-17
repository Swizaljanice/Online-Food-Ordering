package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.dto.RestaurantUserRequestDTO;
import com.online.foodOrderingSystem.dto.RestaurantUserResponseDTO;
import com.online.foodOrderingSystem.service.RestaurantUserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/foodOrderingSystem/users")
public class RestaurantUserController {

    @Autowired
    private RestaurantUserService userService;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/register")
    public ResponseEntity<RestaurantUserResponseDTO> registerUser(
            @Valid @RequestBody RestaurantUserRequestDTO request) {
        log.info("Register user request received: email={}, role={}, name={}",
                request.getEmail(), request.getRole(), request.getName());

        RestaurantUserResponseDTO response = userService.registerUser(request);

        log.info("User registered successfully: id={}, email={}, role={}",
                response.getId(), response.getEmail(), response.getRole());
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<RestaurantUserResponseDTO>> getAllUsers() {
        log.info("Fetching all registered users");

        List<RestaurantUserResponseDTO> users = userService.getAllUsers();

        log.info("Found {} users", users.size());
        return ResponseEntity.ok(users);
    }
}
