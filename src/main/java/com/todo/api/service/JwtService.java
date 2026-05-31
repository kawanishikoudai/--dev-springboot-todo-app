package com.todo.api.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Base64;
import java.util.Date;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String email) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));

        return Jwts.builder()
            .subject(email)
            .issuedAt(new Date())
            .expiration(
                new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)
            )
            .signWith(key)
            .compact();
    }

    public String extractEmail(String token) {
        SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));

        return Jwts.parser()
            .verifyWith(key)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }
}
