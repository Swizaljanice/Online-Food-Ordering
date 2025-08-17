package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.service.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;


@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);


    public static class PaymentRequest {
        private Long orderId;
        private BigDecimal amount;
        public Long getOrderId() { return orderId; }
        public void setOrderId(Long orderId) { this.orderId = orderId; }
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
    }

    @PostMapping("/razorpay")
    public ResponseEntity<?> startRazorpay(@RequestBody PaymentRequest request) {
        log.info("Received Razorpay start request: orderId={}, amount={}",
                request.getOrderId(), request.getAmount());

        try {
            Map<String, Object> data = paymentService.createRazorpayOrder(request.getOrderId(), request.getAmount());
            log.info("Razorpay order created successfully for orderId={} -> {}", request.getOrderId(), data);
            return ResponseEntity.ok(data);
        } catch (Exception e) {
            log.error("Error creating Razorpay order for orderId={}: {}", request.getOrderId(), e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/razorpay/webhook")
    public ResponseEntity<Map<String, Object>> handleRazorpayWebhook(HttpServletRequest request) throws IOException {
        log.info("Received Razorpay webhook callback");

        String payload = request.getReader().lines().reduce("", (acc, line) -> acc + line);
        log.debug("Webhook payload ({} chars)", payload.length()); // Avoid printing full payload to logs unless needed

        Map<String, Object> response = paymentService.handleRazorpayWebhook(payload);
        log.info("Webhook processed, response: {}", response);

        return ResponseEntity.ok(response);
    }
}
