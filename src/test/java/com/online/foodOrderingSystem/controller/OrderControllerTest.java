package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.dto.OrderDto;
import com.online.foodOrderingSystem.dto.OrderResponseDto;
import com.online.foodOrderingSystem.dto.OrderTrackingDto;
import com.online.foodOrderingSystem.entity.Order;
import com.online.foodOrderingSystem.service.OrderService;
import com.online.foodOrderingSystem.util.CustomUserDetails;
import com.online.foodOrderingSystem.util.UpdateStatusRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    @SuppressWarnings("unchecked")
    private Authentication mockAuth(Long userId, String role) {
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getId()).thenReturn(userId);

        // Use doReturn to avoid wildcard capture generics issue
        // Lenient so tests that don't invoke getAuthorities won't fail due to unnecessary stubbing
        lenient().doReturn((Collection<? extends GrantedAuthority>)
                        List.of(new SimpleGrantedAuthority(role)))
                .when(userDetails).getAuthorities();

        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(userDetails);
        return auth;
    }

    @Test
    void checkout_ReturnsOrderDto() {
        OrderDto dto = new OrderDto();
        dto.setId(1L);
        when(orderService.checkout(5L, "Razorpay")).thenReturn(dto);

        OrderController.CheckoutRequest req = new OrderController.CheckoutRequest();
        req.setPaymentMethod("Razorpay");

        ResponseEntity<OrderDto> response =
                orderController.checkout(mockAuth(5L, "ROLE_CUSTOMER"), req);

        assertEquals(1L, response.getBody().getId());
    }

    @Test
    void getOrderDetails_Found_ReturnsDto() {
        Order order = new Order();
        when(orderService.getOrderById(eq(10L), anyLong()))
                .thenReturn(Optional.of(order));
        OrderResponseDto dto = new OrderResponseDto();
        when(orderService.mapToOrderResponseDto(order)).thenReturn(dto);

        ResponseEntity<OrderResponseDto> response =
                orderController.getOrderDetails(mockAuth(1L, "ROLE_CUSTOMER"), 10L);

        assertEquals(dto, response.getBody());
    }

    @Test
    void getOrderDetails_NotFound_Returns404() {
        when(orderService.getOrderById(eq(10L), anyLong()))
                .thenReturn(Optional.empty());

        ResponseEntity<OrderResponseDto> response =
                orderController.getOrderDetails(mockAuth(1L, "ROLE_CUSTOMER"), 10L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void getOrderStatus_Success() {
        when(orderService.getOrderStatus(10L, 1L)).thenReturn("Shipped");

        ResponseEntity<String> response =
                orderController.getOrderStatus(mockAuth(1L, "ROLE_CUSTOMER"), 10L);

        assertEquals("Shipped", response.getBody());
    }

    @Test
    void getOrderStatus_NotFound() {
        when(orderService.getOrderStatus(10L, 1L))
                .thenThrow(new RuntimeException("Not found"));

        ResponseEntity<String> response =
                orderController.getOrderStatus(mockAuth(1L, "ROLE_CUSTOMER"), 10L);

        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void updateOrderStatus_ReturnsUpdatedDto() {
        OrderResponseDto dto = new OrderResponseDto();
        when(orderService.updateOrderStatus(5L, "Delivered")).thenReturn(dto);

        UpdateStatusRequest req = new UpdateStatusRequest();
        req.status = "Delivered";

        ResponseEntity<OrderResponseDto> response =
                orderController.updateOrderStatus(5L, req);

        assertEquals(dto, response.getBody());
    }

    @Test
    void getOrderTracking_ReturnsList() {
        OrderTrackingDto t = new OrderTrackingDto();
        t.setStatus("Packed");
        when(orderService.getOrderTrackingHistory(eq(10L), eq(1L)))
                .thenReturn(List.of(t));

        ResponseEntity<List<OrderTrackingDto>> response =
                orderController.getOrderTracking(mockAuth(1L, "ROLE_CUSTOMER"), 10L);

        assertEquals(1, response.getBody().size());
        assertEquals("Packed", response.getBody().get(0).getStatus());
    }
}
