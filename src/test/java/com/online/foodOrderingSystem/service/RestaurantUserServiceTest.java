package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.dto.RestaurantUserRequestDTO;
import com.online.foodOrderingSystem.dto.RestaurantUserResponseDTO;
import com.online.foodOrderingSystem.entity.RestaurantUser;
import com.online.foodOrderingSystem.repository.RestaurantUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantUserServiceTest {

    @Mock
    private RestaurantUserRepository userRepository;

    @InjectMocks
    private RestaurantUserService userService;

    @Test
    void registerUser_Success() {
        RestaurantUserRequestDTO request = new RestaurantUserRequestDTO();
        request.setName("John");
        request.setEmail("john@example.com");
        request.setPassword("pass123");
        request.setRole("CUSTOMER");
        request.setPhone("1234567890");

        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);

        RestaurantUser savedUser = RestaurantUser.builder()
                .id(1L)
                .name("John")
                .email("john@example.com")
                .password("encodedPass")
                .role(RestaurantUser.Role.CUSTOMER)
                .phone("1234567890")
                .createdAt(LocalDateTime.now()) // initialize createdAt
                .build();

        when(userRepository.save(any(RestaurantUser.class))).thenReturn(savedUser);

        RestaurantUserResponseDTO response = userService.registerUser(request);

        assertEquals("John", response.getName());
        assertEquals("john@example.com", response.getEmail());
    }

    @Test
    void registerUser_EmailExists_ThrowsException() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);
        RestaurantUserRequestDTO request = new RestaurantUserRequestDTO();
        request.setEmail("john@example.com");

        assertThrows(RuntimeException.class, () -> userService.registerUser(request));
    }

    @Test
    void getAllUsers_ReturnsMappedDTOs() {
        List<RestaurantUser> users = List.of(
                RestaurantUser.builder()
                        .id(1L)
                        .name("John")
                        .email("john@example.com")
                        .role(RestaurantUser.Role.CUSTOMER)
                        .phone("1234567890")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        when(userRepository.findAll()).thenReturn(users);

        List<RestaurantUserResponseDTO> result = userService.getAllUsers();
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getName());
    }
}
