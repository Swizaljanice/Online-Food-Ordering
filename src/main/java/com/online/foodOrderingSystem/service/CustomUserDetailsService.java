package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.controller.AuthController;
import com.online.foodOrderingSystem.entity.RestaurantUser;
import com.online.foodOrderingSystem.repository.RestaurantUserRepository;
import com.online.foodOrderingSystem.util.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private RestaurantUserRepository userRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("Attempting to load user by email: {}", email);

        RestaurantUser user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.warn("User not found with email={}", email);
                    return new UsernameNotFoundException("User not found");
                });

        log.info("User found: id={}, role={}, email={}", user.getId(), user.getRole(), user.getEmail());
        return new CustomUserDetails(user);
    }
}
