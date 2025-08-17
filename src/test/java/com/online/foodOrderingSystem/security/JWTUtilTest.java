package com.online.foodOrderingSystem.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JWTUtilTest {

    private final JWTUtil jwtUtil = new JWTUtil();

    @Test
    void generateToken_ValidData_TokenGenerated() {
        String token = jwtUtil.generateToken("test@example.com", "CUSTOMER");
        assertNotNull(token);
    }

    @Test
    void extractEmail_ReturnsOriginalEmail() {
        String token = jwtUtil.generateToken("test@example.com", "CUSTOMER");
        String email = jwtUtil.extractEmail(token);
        assertEquals("test@example.com", email);
    }

    @Test
    void validateToken_CorrectToken_ReturnsTrue() {
        String token = jwtUtil.generateToken("test@example.com", "CUSTOMER");
        assertTrue(jwtUtil.validateToken(token));
    }

    @Test
    void validateToken_InvalidToken_ReturnsFalse() {
        String token = "abc.def.ghi"; // fake token
        assertFalse(jwtUtil.validateToken(token));
    }
}
