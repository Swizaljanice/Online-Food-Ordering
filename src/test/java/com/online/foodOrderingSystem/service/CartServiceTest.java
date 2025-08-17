package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.dto.CartDto;
import com.online.foodOrderingSystem.entity.Cart;
import com.online.foodOrderingSystem.entity.CartItem;
import com.online.foodOrderingSystem.repository.CartItemRepository;
import com.online.foodOrderingSystem.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void addOrUpdateCartItem_AddsNewItem() {
        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setItems(new ArrayList<>());

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));

        CartDto result = cartService.addOrUpdateCartItem(1L, 101L, 2);

        assertEquals(1, result.getItems().size());
        assertEquals(101L, result.getItems().get(0).getMenuItemId());
        assertEquals(2, result.getItems().get(0).getQuantity());
    }

    @Test
    void addOrUpdateCartItem_UpdatesExistingItem() {
        CartItem existing = new CartItem();
        existing.setMenuItemId(101L);
        existing.setQuantity(1);

        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setItems(new ArrayList<>(List.of(existing)));

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));

        CartDto result = cartService.addOrUpdateCartItem(1L, 101L, 3);

        assertEquals(4, result.getItems().get(0).getQuantity());
    }

    @Test
    void updateCartItemQuantity_Success() {
        CartItem existing = new CartItem();
        existing.setId(10L);
        existing.setQuantity(1);

        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setItems(List.of(existing));

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));

        CartDto result = cartService.updateCartItemQuantity(1L, 10L, 5);

        assertEquals(5, result.getItems().get(0).getQuantity());
    }

    @Test
    void updateCartItemQuantity_ItemNotFound_Throws() {
        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setItems(new ArrayList<>());

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        assertThrows(RuntimeException.class,
                () -> cartService.updateCartItemQuantity(1L, 999L, 5));
    }

    @Test
    void removeCartItem_Success() {
        CartItem existing = new CartItem();
        existing.setId(10L);

        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setItems(new ArrayList<>(List.of(existing)));

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));

        CartDto result = cartService.removeCartItem(1L, 10L);

        assertEquals(0, result.getItems().size());
    }

    @Test
    void removeCartItem_ItemNotFound_Throws() {
        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setItems(new ArrayList<>());

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        assertThrows(RuntimeException.class,
                () -> cartService.removeCartItem(1L, 10L));
    }

    @Test
    void getCartByUserId_ReturnsCart() {
        CartItem item = new CartItem();
        item.setId(1L);
        item.setMenuItemId(101L);
        item.setQuantity(2);

        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setId(50L);
        cart.setItems(List.of(item));

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        CartDto dto = cartService.getCartByUserId(1L);

        assertEquals(50L, dto.getId());
        assertEquals(1, dto.getItems().size());
    }

    @Test
    void checkout_ClearsItems() {
        CartItem item = new CartItem();
        item.setId(1L);

        Cart cart = new Cart();
        cart.setUserId(1L);
        cart.setItems(new ArrayList<>(List.of(item)));

        when(cartRepository.findByUserId(1L)).thenReturn(Optional.of(cart));

        cartService.checkout(1L);

        assertEquals(0, cart.getItems().size());
    }
}
