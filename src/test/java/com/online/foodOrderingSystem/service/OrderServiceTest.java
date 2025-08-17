package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.dto.OrderDto;
import com.online.foodOrderingSystem.dto.OrderResponseDto;
import com.online.foodOrderingSystem.entity.*;
import com.online.foodOrderingSystem.repository.MenuItemRepository;
import com.online.foodOrderingSystem.repository.OrderItemRepository;
import com.online.foodOrderingSystem.repository.OrderRepository;
import com.online.foodOrderingSystem.repository.OrderTrackingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderTrackingRepository orderTrackingRepository;
    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private CartService cartService;

    @InjectMocks
    private OrderService orderService;



    @Test
    void getOrderById_Success() {
        Order order = new Order();
        order.setUserId(100L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Optional<Order> result = orderService.getOrderById(1L, 100L);
        assertTrue(result.isPresent());
    }

    @Test
    void getOrderById_AccessDenied_Throws() {
        Order order = new Order();
        order.setUserId(200L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertThrows(RuntimeException.class, () -> orderService.getOrderById(1L, 100L));
    }

    @Test
    void getOrderStatus_Success() {
        Order order = new Order();
        order.setUserId(10L);
        order.setStatus("Pending");
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        assertEquals("Pending", orderService.getOrderStatus(1L, 10L));
    }

    @Test
    void getOrderStatus_NotFound_Throws() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> orderService.getOrderStatus(1L, 10L));
    }

    @Test
    void updateOrderStatus_Success() {
        Order order = new Order();
        order.setId(1L);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> inv.getArgument(0));

        OrderResponseDto result = orderService.updateOrderStatus(1L, "Shipped");

        assertEquals("Shipped", result.getStatus());
        verify(orderTrackingRepository).save(any(OrderTracking.class));
    }

    @Test
    void checkout_Success() {
        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());
        CartItem ci = new CartItem();
        ci.setMenuItemId(5L);
        ci.setQuantity(2);
        cart.getItems().add(ci);

        MenuItem menuItem = new MenuItem();
        menuItem.setId(5L);
        menuItem.setName("Burger");
        menuItem.setPrice(BigDecimal.valueOf(50));
        MenuCategory mc = new MenuCategory();
        Restaurant r = new Restaurant();
        r.setId(999L);
        mc.setRestaurant(r);
        menuItem.setCategory(mc);

        when(cartService.getOrCreateCartByUserId(1L)).thenReturn(cart);
        when(menuItemRepository.findById(5L)).thenReturn(Optional.of(menuItem));
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order o = inv.getArgument(0);
            o.setId(10L);
            o.setTotalAmount(BigDecimal.valueOf(100));
            return o;
        });

        OrderDto dto = orderService.checkout(1L, "COD");
        assertEquals(10L, dto.getId());
        assertEquals(BigDecimal.valueOf(100), dto.getTotalAmount());
        verify(orderTrackingRepository).save(any(OrderTracking.class));
    }

    @Test
    void checkout_EmptyCart_Throws() {
        Cart cart = new Cart();
        cart.setItems(Collections.emptyList());
        when(cartService.getOrCreateCartByUserId(1L)).thenReturn(cart);
        assertThrows(RuntimeException.class, () -> orderService.checkout(1L, "COD"));
    }

    @Test
    void checkout_MenuItemNotFound_Throws() {
        Cart cart = new Cart();
        CartItem ci = new CartItem();
        ci.setMenuItemId(5L);
        ci.setQuantity(1);
        cart.setItems(List.of(ci));

        when(cartService.getOrCreateCartByUserId(1L)).thenReturn(cart);
        when(menuItemRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.checkout(1L, "COD"));
    }

    @Test
    void getOrderTrackingHistory_Success() {
        OrderTracking tracking = new OrderTracking();
        tracking.setStatus("Pending");
        tracking.setUpdatedAt(LocalDateTime.now());

        Order order = new Order();
        order.setUserId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderTrackingRepository.findByOrderIdOrderByUpdatedAtAsc(1L)).thenReturn(List.of(tracking));

        var history = orderService.getOrderTrackingHistory(1L, 1L);
        assertEquals(1, history.size());
        assertEquals("Pending", history.get(0).getStatus());
    }
}
