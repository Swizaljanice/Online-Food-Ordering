package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.dto.RestaurantUserRequestDTO;
import com.online.foodOrderingSystem.dto.RestaurantUserResponseDTO;
import com.online.foodOrderingSystem.service.RestaurantUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RestaurantUserControllerTest {

    @Mock
    private RestaurantUserService userService;

    @InjectMocks
    private RestaurantUserController controller;

    @Test
    void registerUser_ReturnsResponseDTO() {
        RestaurantUserRequestDTO req = new RestaurantUserRequestDTO();
        req.setName("John");
        req.setEmail("john@example.com");
        req.setPassword("pass");
        req.setRole("CUSTOMER");
        req.setPhone("123456");

        RestaurantUserResponseDTO respDto = RestaurantUserResponseDTO.builder()
                .id(1L).name("John").email("john@example.com").role("CUSTOMER")
                .phone("123456").build();

        when(userService.registerUser(req)).thenReturn(respDto);

        ResponseEntity<RestaurantUserResponseDTO> response = controller.registerUser(req);

        assertEquals(respDto, response.getBody());
        verify(userService).registerUser(req);
    }

    @Test
    void getAllUsers_ReturnsList() {
        List<RestaurantUserResponseDTO> list = List.of(RestaurantUserResponseDTO.builder().id(1L).build());
        when(userService.getAllUsers()).thenReturn(list);

        ResponseEntity<List<RestaurantUserResponseDTO>> resp = controller.getAllUsers();

        assertEquals(list, resp.getBody());
        verify(userService).getAllUsers();
    }
}
