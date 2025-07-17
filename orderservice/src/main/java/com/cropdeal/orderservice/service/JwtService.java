package com.cropdeal.orderservice.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    private Key key;

    @PostConstruct
    public void init() {
        // Decode the Base64-encoded secret and initialize the signing key
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject(); // typically holds the email
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("Role", String.class); // optional if needed
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
