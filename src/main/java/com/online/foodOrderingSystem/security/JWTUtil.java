package com.online.foodOrderingSystem.security;

import com.online.foodOrderingSystem.controller.AuthController;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class JWTUtil {
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    private final String SECRET = "S3cUr3R@nD0mS3cr3tK3yV@l#1234567890ABCDEF";
    private final long EXPIRATION = 1000 * 60 * 60;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(String email, String role) {
        log.debug("Generating token for email: {}, role: {}", email, role);
        String token = Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        log.debug("Token generated successfully for {}: {}", email, token);
        return token;
    }


    public String extractEmail(String token) {
        String email = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        log.debug("Email extracted from token: {}", email);
        return email;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
            log.debug("Token validation succeeded");
            return true;
        } catch (JwtException e) {
            log.warn("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
}
