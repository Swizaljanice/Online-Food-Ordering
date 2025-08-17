package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.security.JWTUtil;
import com.online.foodOrderingSystem.entity.RestaurantUser;
import com.online.foodOrderingSystem.repository.RestaurantUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/foodOrderingSystem/auth")
public class AuthController {
    private AuthenticationManager authenticationManager;
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private RestaurantUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestParam String email, @RequestParam String password) {
        log.info("Login attempt for email: {}", email);
        RestaurantUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("Login failed - email not found: {}", email);
                    return new RuntimeException("Invalid email or password");
                });

        log.debug("User found: {} with role {}", user.getEmail(), user.getRole());

        if (!passwordEncoder.matches(password, user.getPassword())) {
            log.warn("Login failed - invalid password for email: {}", email);
            throw new RuntimeException("Invalid email or password");
        }
        log.info("Password matched for email: {}", email);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        log.info("JWT generated for {}: {}", email, token);

        return ResponseEntity.ok(token);
    }

}
