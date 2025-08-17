package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.entity.RestaurantUser;
import com.online.foodOrderingSystem.repository.RestaurantUserRepository;
import com.online.foodOrderingSystem.util.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomUserDetailsServiceTest {

    @Mock
    private RestaurantUserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @Test
    void loadUserByUsername_UserExists_ReturnsUserDetails() {
        RestaurantUser user = new RestaurantUser();
        user.setEmail("test@example.com");
        user.setPassword("encodedPassword");
        user.setRole(RestaurantUser.Role.CUSTOMER);

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        UserDetails result = customUserDetailsService.loadUserByUsername("test@example.com");

        assertNotNull(result);
        assertTrue(result instanceof CustomUserDetails);
        assertEquals("test@example.com", result.getUsername());
    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsException() {
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> customUserDetailsService.loadUserByUsername("missing@example.com"));
    }
}
