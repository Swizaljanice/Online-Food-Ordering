package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.dto.OrderResponseDto;
import com.online.foodOrderingSystem.entity.Cart;
import com.online.foodOrderingSystem.entity.Order;
import com.online.foodOrderingSystem.entity.PaymentTransaction;
import com.online.foodOrderingSystem.repository.CartRepository;
import com.online.foodOrderingSystem.repository.OrderRepository;
import com.online.foodOrderingSystem.repository.PaymentTransactionRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentTransactionRepository paymentRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartService cartService;

    @Mock
    private OrderService orderService;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() throws Exception {
        // If PaymentService.init() performs real Razorpay client creation,
        // You may want to skip/override it or mock razorpayClient inside your tests.
        // Otherwise comment out if no real external call should happen.
    }

    @Test
    void handleWebhook_PaymentCaptured_OrderUpdated() throws JSONException{
        String txId = "rzp_order_123";

        PaymentTransaction tx = new PaymentTransaction();
        tx.setOrderId(1L);
        tx.setTransactionId(txId);

        Order order = new Order();
        order.setId(1L);
        order.setUserId(10L);

        Cart cart = new Cart();
        cart.setItems(new ArrayList<>());

        when(paymentRepository.findByTransactionId(txId)).thenReturn(Optional.of(tx));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(cartService.getOrCreateCartByUserId(10L)).thenReturn(cart);
        when(orderService.mapToOrderResponseDto(order)).thenReturn(new OrderResponseDto());

        JSONObject json = new JSONObject()
                .put("event", "payment.captured")
                .put("payload", new JSONObject()
                        .put("payment", new JSONObject()
                                .put("entity", new JSONObject().put("order_id", txId))));

        Map<String, Object> resp = paymentService.handleRazorpayWebhook(json.toString());

        assertEquals("Order placed successfully", resp.get("message"));
        verify(paymentRepository).save(any(PaymentTransaction.class));
        verify(orderRepository).save(order);
        verify(cartRepository).save(cart);
    }

    @Test
    void handleWebhook_PaymentFailed_StatusUpdated() throws JSONException{
        String txId = "rzp_order_fail";

        PaymentTransaction tx = new PaymentTransaction();
        tx.setTransactionId(txId);

        when(paymentRepository.findByTransactionId(txId)).thenReturn(Optional.of(tx));

        JSONObject json = new JSONObject()
                .put("event", "payment.failed")
                .put("payload", new JSONObject()
                        .put("payment", new JSONObject()
                                .put("entity", new JSONObject().put("order_id", txId))));

        Map<String, Object> resp = paymentService.handleRazorpayWebhook(json.toString());

        assertEquals("Payment failed", resp.get("message"));
        verify(paymentRepository).save(tx);
    }

    @Test
    void handleWebhook_UnhandledEventType() throws JSONException {
        JSONObject json = new JSONObject().put("event", "other.event");
        Map<String, Object> resp = paymentService.handleRazorpayWebhook(json.toString());
        assertEquals("Unhandled event type", resp.get("message"));
    }


    @Test
    void handleWebhook_InvalidPayload_Throws() {
        assertThrows(RuntimeException.class, () -> paymentService.handleRazorpayWebhook("invalid-json-payload"));
    }
}
