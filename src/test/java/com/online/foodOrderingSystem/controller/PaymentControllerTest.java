package com.online.foodOrderingSystem.controller;

import com.online.foodOrderingSystem.service.PaymentService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock private PaymentService paymentService;
    @InjectMocks private PaymentController paymentController;

    @Test
    void startRazorpay_Success() throws Exception {
        PaymentController.PaymentRequest req = new PaymentController.PaymentRequest();
        req.setOrderId(1L);
        req.setAmount(BigDecimal.valueOf(100));

        Map<String, Object> serviceResp = Map.of("razorpayOrderId", "id_123");
        when(paymentService.createRazorpayOrder(1L, BigDecimal.valueOf(100))).thenReturn(serviceResp);

        ResponseEntity<?> resp = paymentController.startRazorpay(req);
        assertEquals(serviceResp, resp.getBody());
    }

    @Test
    void startRazorpay_ErrorHandled() throws Exception {
        PaymentController.PaymentRequest req = new PaymentController.PaymentRequest();
        req.setOrderId(1L);
        req.setAmount(BigDecimal.valueOf(100));

        when(paymentService.createRazorpayOrder(anyLong(), any()))
                .thenThrow(new RuntimeException("fail"));

        ResponseEntity<?> resp = paymentController.startRazorpay(req);
        assertEquals(500, resp.getStatusCodeValue());
    }

    @Test
    void handleWebhook_ReturnsServiceResponse() throws Exception {
        HttpServletRequest servletRequest = mock(HttpServletRequest.class);
        when(servletRequest.getReader()).thenReturn(new BufferedReader(new StringReader("{\"event\":\"test\"}")));

        Map<String, Object> respMap = Map.of("message", "ok");
        when(paymentService.handleRazorpayWebhook(anyString())).thenReturn(respMap);

        ResponseEntity<Map<String, Object>> resp = paymentController.handleRazorpayWebhook(servletRequest);
        assertEquals(respMap, resp.getBody());
    }
}
