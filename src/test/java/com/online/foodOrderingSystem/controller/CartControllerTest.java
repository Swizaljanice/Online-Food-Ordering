package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.dto.CartDto;
import com.online.foodOrderingSystem.service.CartService;
import com.online.foodOrderingSystem.service.OrderService;
import com.online.foodOrderingSystem.util.CustomUserDetails;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;
    @Mock
    private OrderService orderService;

    @InjectMocks
    private CartController cartController;

    @Test
    void getCart_ReturnsDto() {
        CartDto dto = new CartDto();
        dto.setId(1L);

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(5L);
        when(cartService.getCartByUserId(5L)).thenReturn(dto);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);

        ResponseEntity<CartDto> response = cartController.getCart(auth);

        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void addItemToCart_CallsService() {
        CartDto dto = new CartDto();
        dto.setId(2L);

        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(5L);

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);

        when(cartService.addOrUpdateCartItem(5L, 101L, 2)).thenReturn(dto);

        ResponseEntity<CartDto> response = cartController.addItemToCart(auth, 101L, 2);

        assertEquals(2L, response.getBody().getId());
    }
}
