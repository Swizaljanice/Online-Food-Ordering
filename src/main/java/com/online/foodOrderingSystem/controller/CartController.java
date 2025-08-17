package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.dto.CartDto;
import com.online.foodOrderingSystem.dto.OrderDto;
import com.online.foodOrderingSystem.service.CartService;
import com.online.foodOrderingSystem.service.OrderService;
import com.online.foodOrderingSystem.util.CustomUserDetails;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private OrderService orderService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @GetMapping("/getCart")
    public ResponseEntity<CartDto> getCart(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        log.info("Fetching cart for userId={}", userId);

        CartDto cart = cartService.getCartByUserId(userId);
        log.debug("Cart fetched for userId={} -> {}", userId, cart);

        return ResponseEntity.ok(cart);
    }

    @PostMapping("/addItemToCart")
    public ResponseEntity<CartDto> addItemToCart(Authentication authentication,
                                                 @RequestParam Long menuItemId,
                                                 @RequestParam int quantity) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        log.info("Adding item to cart: userId={}, menuItemId={}, quantity={}",
                userId, menuItemId, quantity);

        CartDto updatedCart = cartService.addOrUpdateCartItem(userId, menuItemId, quantity);
        log.debug("Updated cart after adding item: {}", updatedCart);

        return ResponseEntity.ok(updatedCart);
    }

    @PutMapping("/updateCartItem")
    public ResponseEntity<CartDto> updateCartItem(Authentication authentication,
                                                  @RequestParam Long cartItemId,
                                                  @RequestParam int quantity) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        log.info("Updating cart item: userId={}, cartItemId={}, quantity={}",
                userId, cartItemId, quantity);

        CartDto updatedCart = cartService.updateCartItemQuantity(userId, cartItemId, quantity);
        log.debug("Updated cart after item quantity change: {}", updatedCart);

        return ResponseEntity.ok(updatedCart);
    }

    @DeleteMapping("/removeCartItem/{cartItemId}")
    public ResponseEntity<CartDto> removeCartItem(Authentication authentication,
                                                  @PathVariable Long cartItemId) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        log.info("Removing cart item: userId={}, cartItemId={}", userId, cartItemId);

        CartDto updatedCart = cartService.removeCartItem(userId, cartItemId);
        log.debug("Updated cart after removing item: {}", updatedCart);

        return ResponseEntity.ok(updatedCart);
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping("/checkout")
    public ResponseEntity<OrderDto> checkout(Authentication authentication,
                                             @RequestBody OrderController.CheckoutRequest request) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        log.info("Checkout initiated for userId={} with paymentMethod={}",
                userId, request.getPaymentMethod());

        OrderDto order = orderService.checkout(userId, request.getPaymentMethod());
        log.info("Checkout completed for userId={} -> orderId={}", userId, order.getId());

        return ResponseEntity.ok(order);
    }
}
