package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.controller.AuthController;
import com.online.foodOrderingSystem.dto.CartDto;
import com.online.foodOrderingSystem.dto.CartItemDto;
import com.online.foodOrderingSystem.entity.Cart;
import com.online.foodOrderingSystem.entity.CartItem;
import com.online.foodOrderingSystem.repository.CartItemRepository;
import com.online.foodOrderingSystem.repository.CartRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);
    public Cart getOrCreateCartByUserId(Long userId) {
        log.info("Fetching cart for userId={}", userId);
        return cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    log.info("No cart found for userId={}, creating new cart", userId);
                    Cart cart = new Cart();
                    cart.setUserId(userId);
                    Cart saved = cartRepository.save(cart);
                    log.debug("New cart created: {}", saved);
                    return saved;
                });
    }

    public CartDto addOrUpdateCartItem(Long userId, Long menuItemId, int quantity) {
        log.info("Adding/Updating cart item: userId={}, menuItemId={}, quantity={}", userId, menuItemId, quantity);

        Cart cart = getOrCreateCartByUserId(userId);

        List<CartItem> items = cart.getItems();
        CartItem existingItem = items.stream()
                .filter(i -> i.getMenuItemId().equals(menuItemId))
                .findFirst()
                .orElse(null);

        if (existingItem != null) {
            log.debug("Item already exists in cart. Incrementing quantity. CurrentQty={}, Adding={}", existingItem.getQuantity(), quantity);
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
        } else {
            log.debug("Item not found in cart. Adding new item.");
            CartItem newItem = new CartItem();
            newItem.setMenuItemId(menuItemId);
            newItem.setQuantity(quantity);
            cart.addItem(newItem);
        }

        Cart savedCart = cartRepository.save(cart);
        log.debug("Cart saved after add/update: {}", savedCart);

        return mapToCartDto(savedCart);
    }

    public CartDto updateCartItemQuantity(Long userId, Long cartItemId, int quantity) {
        log.info("Updating cart item quantity: userId={}, cartItemId={}, newQuantity={}", userId, cartItemId, quantity);

        Cart cart = getOrCreateCartByUserId(userId);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("CartItem not found for cartItemId={} and userId={}", cartItemId, userId);
                    return new RuntimeException("CartItem not found");
                });

        item.setQuantity(quantity);
        Cart saved = cartRepository.save(cart);
        log.debug("Cart saved after quantity update: {}", saved);

        return mapToCartDto(saved);
    }

    public CartDto removeCartItem(Long userId, Long cartItemId) {
        log.info("Removing cart item: userId={}, cartItemId={}", userId, cartItemId);

        Cart cart = getOrCreateCartByUserId(userId);

        CartItem item = cart.getItems().stream()
                .filter(i -> i.getId().equals(cartItemId))
                .findFirst()
                .orElseThrow(() -> {
                    log.warn("CartItem not found for removal: cartItemId={} userId={}", cartItemId, userId);
                    return new RuntimeException("CartItem not found");
                });

        cart.removeItem(item);
        Cart savedCart = cartRepository.save(cart);
        log.debug("Cart saved after removing item: {}", savedCart);

        return mapToCartDto(savedCart);
    }

    public CartDto getCartByUserId(Long userId) {
        log.info("Retrieving cart for userId={}", userId);
        Cart cart = getOrCreateCartByUserId(userId);
        return mapToCartDto(cart);
    }

    public void checkout(Long userId) {
        log.info("Checkout started for userId={}", userId);
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> {
                    log.error("Checkout failed: Cart not found for userId={}", userId);
                    return new RuntimeException("Cart not found");
                });
        cart.getItems().clear();
        cartRepository.save(cart);
        log.info("Cart cleared for userId={} after checkout", userId);
    }

    public Cart saveCart(Cart cart) {
        log.debug("Saving cart: {}", cart);
        return cartRepository.save(cart);
    }

    private CartDto mapToCartDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setUserId(cart.getUserId());
        dto.setCreatedAt(cart.getCreatedAt());
        dto.setItems(cart.getItems().stream()
                .map(i -> {
                    CartItemDto itemDto = new CartItemDto();
                    itemDto.setId(i.getId());
                    itemDto.setMenuItemId(i.getMenuItemId());
                    itemDto.setQuantity(i.getQuantity());
                    return itemDto;
                }).toList());
        return dto;
    }
}
