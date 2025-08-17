package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.controller.AuthController;
import com.online.foodOrderingSystem.dto.OrderDto;
import com.online.foodOrderingSystem.dto.OrderItemDto;
import com.online.foodOrderingSystem.dto.OrderResponseDto;
import com.online.foodOrderingSystem.dto.OrderTrackingDto;
import com.online.foodOrderingSystem.entity.*;
import com.online.foodOrderingSystem.repository.MenuItemRepository;
import com.online.foodOrderingSystem.repository.OrderItemRepository;
import com.online.foodOrderingSystem.repository.OrderRepository;
import com.online.foodOrderingSystem.repository.OrderTrackingRepository;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private OrderTrackingRepository orderTrackingRepository;
    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private CartService cartService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    public OrderService(OrderRepository orderRepository,
                        OrderTrackingRepository orderTrackingRepository,
                        MenuItemRepository menuItemRepository,
                        OrderItemRepository orderItemRepository,
                        CartService cartService) {
        this.orderRepository = orderRepository;
        this.orderTrackingRepository = orderTrackingRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
    }

    public Optional<Order> getOrderById(Long orderId, Long userId) {
        log.info("Fetching order: orderId={}, userId={}", orderId, userId);
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent() && !order.get().getUserId().equals(userId)) {
            log.warn("Access denied for userId={} to orderId={}", userId, orderId);
            throw new RuntimeException("Access denied");
        }
        log.info("Order {} found for userId={}", orderId, userId);
        return order;
    }

    public String getOrderStatus(Long orderId, Long userId) {
        log.info("Fetching order status: orderId={}, userId={}", orderId, userId);
        return getOrderById(orderId, userId)
                .map(Order::getStatus)
                .orElseThrow(() -> {
                    log.warn("Order not found for userId={} orderId={}", userId, orderId);
                    return new RuntimeException("Order not found");
                });
    }

    public OrderResponseDto updateOrderStatus(Long orderId, String status) {
        log.info("Updating order status: orderId={}, newStatus={}", orderId, status);
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> {
                    log.warn("Order not found with id={}", orderId);
                    return new RuntimeException("Order not found");
                });

        order.setStatus(status);
        Order savedOrder = orderRepository.save(order);

        orderTrackingRepository.save(new OrderTracking(orderId, status));
        log.info("Order status updated: orderId={}, status={}", orderId, status);

        return mapToOrderResponseDto(savedOrder);
    }

    public OrderDto checkout(Long userId, String paymentMethod) {
        log.info("Checkout initiated: userId={}, paymentMethod={}", userId, paymentMethod);
        Cart cart = cartService.getOrCreateCartByUserId(userId);

        if (cart.getItems().isEmpty()) {
            log.warn("Checkout failed - cart is empty for userId={}", userId);
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);

        Long restaurantId = null;
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()) {
            var menuItem = menuItemRepository.findById(cartItem.getMenuItemId())
                    .orElseThrow(() -> {
                        log.warn("MenuItem not found: id={}", cartItem.getMenuItemId());
                        return new RuntimeException("MenuItem not found");
                    });

            if (restaurantId == null) {
                restaurantId = menuItem.getCategory().getRestaurant().getId();
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setMenuItemId(menuItem.getId());
            orderItem.setMenuItemName(menuItem.getName());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(menuItem.getPrice());
            order.addItem(orderItem);

            totalAmount = totalAmount.add(menuItem.getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity())));
        }

        order.setRestaurantId(restaurantId);
        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);
        log.info("Order saved: orderId={}, totalAmount={}", savedOrder.getId(), totalAmount);

        orderTrackingRepository.save(new OrderTracking(savedOrder.getId(), "Pending"));
        log.info("Initial tracking entry added for orderId={} with status='Pending'", savedOrder.getId());

        cart.getItems().clear();
        cartService.saveCart(cart);
        log.info("Cart cleared after checkout for userId={}", userId);

        return mapToOrderDto(savedOrder);
    }

    public OrderDto mapToOrderDto(Order order) {
        log.debug("Mapping Order entity to OrderDto: orderId={}", order.getId());
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setRestaurantId(order.getRestaurantId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setItems(order.getItems().stream().map(item -> {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setId(item.getId());
            itemDto.setMenuItemId(item.getMenuItemId());
            itemDto.setMenuItemName(item.getMenuItemName());
            itemDto.setPrice(item.getPrice());
            itemDto.setQuantity(item.getQuantity());
            return itemDto;
        }).toList());
        return dto;
    }

    public OrderResponseDto mapToOrderResponseDto(Order order) {
        log.debug("Mapping Order entity to OrderResponseDto: orderId={}", order.getId());
        OrderResponseDto dto = new OrderResponseDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUserId());
        dto.setRestaurantId(order.getRestaurantId());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setStatus(order.getStatus());
        dto.setCreatedAt(order.getCreatedAt());
        dto.setItems(order.getItems().stream().map(item -> {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setId(item.getId());
            itemDto.setMenuItemId(item.getMenuItemId());
            itemDto.setMenuItemName(item.getMenuItemName());
            itemDto.setPrice(item.getPrice());
            itemDto.setQuantity(item.getQuantity());
            return itemDto;
        }).toList());
        return dto;
    }

    public List<OrderTrackingDto> getOrderTrackingHistory(Long orderId, Long userId) {
        log.info("Fetching tracking history: orderId={}, userId={}", orderId, userId);
        getOrderById(orderId, userId);

        List<OrderTrackingDto> history = orderTrackingRepository.findByOrderIdOrderByUpdatedAtAsc(orderId)
                .stream()
                .map(track -> {
                    OrderTrackingDto dto = new OrderTrackingDto();
                    dto.setStatus(track.getStatus());
                    dto.setUpdatedAt(track.getUpdatedAt());
                    return dto;
                }).toList();

        log.info("Found {} tracking records for orderId={}", history.size(), orderId);
        return history;
    }
}
