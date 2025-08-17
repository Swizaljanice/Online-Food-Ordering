package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.dto.OrderDto;
import com.online.foodOrderingSystem.dto.OrderResponseDto;
import com.online.foodOrderingSystem.dto.OrderTrackingDto;
import com.online.foodOrderingSystem.util.CustomUserDetails;
import com.online.foodOrderingSystem.util.UpdateStatusRequest;
import com.online.foodOrderingSystem.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public static class CheckoutRequest {
        private String paymentMethod;

        public String getPaymentMethod() { return paymentMethod; }
        public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/checkout")
    public ResponseEntity<OrderDto> checkout(Authentication authentication,
                                             @RequestBody CheckoutRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();

        log.info("Checkout request from userId={} with paymentMethod={}", userId, request.getPaymentMethod());
        OrderDto orderDto = orderService.checkout(userId, request.getPaymentMethod());
        log.info("Checkout successful for userId={} -> orderId={}", userId, orderDto.getId());

        return ResponseEntity.ok(orderDto);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('RESTAURANT_STAFF') or hasRole('ADMIN')")
    @GetMapping("/getOrderDetails/{orderId}")
    public ResponseEntity<OrderResponseDto> getOrderDetails(Authentication authentication,
                                                            @PathVariable Long orderId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("Get order details request: userId={}, orderId={}", userDetails.getId(), orderId);

        return orderService.getOrderById(orderId, userDetails.getId())
                .map(order -> {
                    log.info("Order found: orderId={} for userId={}", orderId, userDetails.getId());
                    return ResponseEntity.ok(orderService.mapToOrderResponseDto(order));
                })
                .orElseGet(() -> {
                    log.warn("Order not found: orderId={} for userId={}", orderId, userDetails.getId());
                    return ResponseEntity.notFound().build();
                });
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('RESTAURANT_STAFF') or hasRole('ADMIN')")
    @GetMapping("/getOrderStatus/{orderId}/status")
    public ResponseEntity<String> getOrderStatus(Authentication authentication,
                                                 @PathVariable Long orderId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("Get order status request: userId={}, orderId={}", userDetails.getId(), orderId);

        try {
            String status = orderService.getOrderStatus(orderId, userDetails.getId());
            log.info("Status for orderId={} is '{}'", orderId, status);
            return ResponseEntity.ok(status);
        } catch (RuntimeException e) {
            log.warn("Order not found or access denied: orderId={} userId={}", orderId, userDetails.getId());
            return ResponseEntity.notFound().build();
        }
    }

    @PreAuthorize("hasRole('RESTAURANT_STAFF') or hasRole('ADMIN')")
    @PutMapping("/updateOrderStatus/{orderId}/status")
    public ResponseEntity<OrderResponseDto> updateOrderStatus(@PathVariable Long orderId,
                                                              @RequestBody UpdateStatusRequest request) {
        log.info("Update order status request: orderId={}, newStatus={}", orderId, request.status);
        OrderResponseDto updated = orderService.updateOrderStatus(orderId, request.status);
        log.info("Order status updated: orderId={}, status={}", orderId, updated.getStatus());
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/{orderId}/tracking")
    public ResponseEntity<List<OrderTrackingDto>> getOrderTracking(Authentication authentication,
                                                                   @PathVariable Long orderId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        log.info("Get order tracking request: userId={}, orderId={}", userDetails.getId(), orderId);

        List<OrderTrackingDto> history = orderService.getOrderTrackingHistory(orderId, userDetails.getId());
        log.info("Found {} tracking updates for orderId={}", history.size(), orderId);

        return ResponseEntity.ok(history);
    }
}
