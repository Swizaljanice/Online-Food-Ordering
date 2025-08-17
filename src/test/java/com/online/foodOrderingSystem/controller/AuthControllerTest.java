package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.entity.RestaurantUser;
import com.online.foodOrderingSystem.repository.RestaurantUserRepository;
import com.online.foodOrderingSystem.security.JWTUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private JWTUtil jwtUtil;

    @Mock
    private RestaurantUserRepository userRepository;

    @InjectMocks
    private AuthController authController;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    void login_Success_ReturnsToken() {
        RestaurantUser user = RestaurantUser.builder()
                .email("test@example.com")
                .password(encoder.encode("pass123"))
                .role(RestaurantUser.Role.CUSTOMER)
                .build();

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(jwtUtil.generateToken(anyString(), anyString())).thenReturn("jwt-token");

        ResponseEntity<?> response = authController.login("test@example.com", "pass123");

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("jwt-token", response.getBody());
    }

    @Test
    void login_EmailNotFound_ThrowsException() {
        when(userRepository.findByEmail("nope@example.com")).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class,
                () -> authController.login("nope@example.com", "pass123"));
    }

    @Test
    void login_InvalidPassword_ThrowsException() {
        RestaurantUser user = RestaurantUser.builder()
                .email("test@example.com")
                .password(encoder.encode("correctpass"))
                .role(RestaurantUser.Role.CUSTOMER)
                .build();
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        assertThrows(RuntimeException.class,
                () -> authController.login("test@example.com", "wrongpass"));
    }
}
