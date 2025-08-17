package com.online.foodOrderingSystem.service;

import com.online.foodOrderingSystem.controller.AuthController;
import com.online.foodOrderingSystem.dto.OrderResponseDto;
import com.online.foodOrderingSystem.entity.Cart;
import com.online.foodOrderingSystem.entity.Order;
import com.online.foodOrderingSystem.entity.PaymentTransaction;
import com.online.foodOrderingSystem.repository.CartRepository;
import com.online.foodOrderingSystem.repository.OrderRepository;
import com.online.foodOrderingSystem.repository.PaymentTransactionRepository;
import com.razorpay.RazorpayClient;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentService {

    @Value("${razorpay.key_id}")
    private String razorpayKeyId;

    @Value("${razorpay.key_secret}")
    private String razorpayKeySecret;

    private RazorpayClient razorpayClient;

    @Autowired
    private PaymentTransactionRepository paymentRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderService orderService;

    @Autowired
    public PaymentService(PaymentTransactionRepository paymentRepository,
                          OrderRepository orderRepository,
                          CartRepository cartRepository,
                          CartService cartService,
                          OrderService orderService) {
        this.paymentRepository = paymentRepository;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
        this.cartService = cartService;
        this.orderService = orderService;
    }
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostConstruct
    public void init() throws Exception {
        log.info("Initializing Razorpay client");
        razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
    }

    public Map<String, Object> createRazorpayOrder(Long orderId, BigDecimal amount) throws Exception {
        log.info("Creating Razorpay order: orderId={}, amount={}", orderId, amount);

        JSONObject orderRequest = new JSONObject();
        orderRequest.put("amount", amount.multiply(BigDecimal.valueOf(100)).intValue()); // in paise
        orderRequest.put("currency", "INR");
        orderRequest.put("receipt", "order_rcptid_" + orderId);
        orderRequest.put("payment_capture", 1);

        com.razorpay.Order razorpayOrder = razorpayClient.Orders.create(orderRequest);
        String razorpayOrderId = String.valueOf(razorpayOrder.get("id"));
        log.info("Razorpay order created successfully: razorpayOrderId={}", razorpayOrderId);

        PaymentTransaction tx = new PaymentTransaction();
        tx.setOrderId(orderId);
        tx.setPaymentMethod("Razorpay");
        tx.setAmount(amount);
        tx.setStatus("Started");
        tx.setTransactionId(razorpayOrder.get("id"));
        paymentRepository.save(tx);
        log.info("Payment transaction saved: transactionId={}, status=Started", tx.getTransactionId());

        Map<String, Object> response = new HashMap<>();
        response.put("razorpayOrderId", razorpayOrder.get("id"));
        response.put("amount", amount);
        response.put("currency", "INR");
        response.put("orderId", orderId);

        return response;
    }

    public Map<String, Object> handleRazorpayWebhook(String payload) {
        log.info("Handling Razorpay webhook payload ({} chars)", payload.length());

        Map<String, Object> response = new HashMap<>();
        try {
            JSONObject eventJson = new JSONObject(payload);
            String eventType = eventJson.getString("event");
            log.info("Webhook event type received: {}", eventType);

            if ("payment.captured".equals(eventType)) {
                processPaymentCaptured(eventJson, response);
            } else if ("payment.failed".equals(eventType)) {
                processPaymentFailed(eventJson, response);
            } else {
                log.warn("Unhandled webhook event type: {}", eventType);
                response.put("message", "Unhandled event type");
            }
        } catch (Exception e) {
            log.error("Error handling Razorpay webhook: {}", e.getMessage(), e);
            throw new RuntimeException("Error handling Razorpay webhook: " + e.getMessage(), e);
        }
        return response;
    }

    private void processPaymentCaptured(JSONObject eventJson, Map<String, Object> response) {
        String razorpayOrderId = eventJson
                .getJSONObject("payload")
                .getJSONObject("payment")
                .getJSONObject("entity")
                .getString("order_id");
        log.info("Processing 'payment.captured' for Razorpay orderId={}", razorpayOrderId);

        PaymentTransaction tx = paymentRepository.findByTransactionId(razorpayOrderId).orElse(null);
        if (tx != null) {
            tx.setStatus("Succeeded");
            paymentRepository.save(tx);
            log.info("Payment transaction updated to Succeeded: transactionId={}", razorpayOrderId);

            Order order = orderRepository.findById(tx.getOrderId()).orElse(null);
            if (order != null) {
                order.setStatus("Paid");
                orderRepository.save(order);
                log.info("Order marked as Paid: orderId={}", order.getId());

                Cart cart = cartService.getOrCreateCartByUserId(order.getUserId());
                cart.getItems().clear();
                cartRepository.save(cart);
                log.info("Cart cleared for userId={} after payment", order.getUserId());

                OrderResponseDto orderDto = orderService.mapToOrderResponseDto(order);
                response.put("message", "Order placed successfully");
                response.put("orderDetails", orderDto);
            } else {
                log.warn("Order not found in DB for orderId={}", tx.getOrderId());
                response.put("message", "Order updated but order details not found");
            }
        } else {
            log.warn("Payment transaction not found for razorpayOrderId={}", razorpayOrderId);
            response.put("message", "Payment transaction not found");
        }
    }

    private void processPaymentFailed(JSONObject eventJson, Map<String, Object> response) {
        String razorpayOrderId = eventJson
                .getJSONObject("payload")
                .getJSONObject("payment")
                .getJSONObject("entity")
                .getString("order_id");
        log.info("Processing 'payment.failed' for Razorpay orderId={}", razorpayOrderId);

        PaymentTransaction tx = paymentRepository.findByTransactionId(razorpayOrderId).orElse(null);
        if (tx != null) {
            tx.setStatus("Failed");
            paymentRepository.save(tx);
            log.info("Payment transaction marked as Failed: transactionId={}", razorpayOrderId);
        } else {
            log.warn("Payment transaction not found for failed webhook: razorpayOrderId={}", razorpayOrderId);
        }
        response.put("message", "Payment failed");
    }
}
