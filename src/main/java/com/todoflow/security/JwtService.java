package com.todoflow.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    private final long ACCESS_TOKEN_VALIDITY =
            1000L * 60 * 15; // 15 dakika

    private final long REFRESH_TOKEN_VALIDITY =
            1000L * 60 * 60 * 24 * 7; // 7 gün

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secretKey.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String generateAccessToken(String username) {

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + ACCESS_TOKEN_VALIDITY
        );

        return Jwts.builder()
                .subject(username)
                .claim("tokenType", "ACCESS")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public String generateRefreshToken(String username) {

        Date now = new Date();

        Date expiration = new Date(
                now.getTime() + REFRESH_TOKEN_VALIDITY
        );

        return Jwts.builder()
                .subject(username)
                .claim("tokenType", "REFRESH")
                .issuedAt(now)
                .expiration(expiration)
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUsername(String token) {
        return extractAllClaims(token).getSubject();
    }

    public boolean isRefreshToken(String token) {
        return "REFRESH".equals(
                extractAllClaims(token)
                        .get("tokenType", String.class)
        );
    }

    public boolean isAccessToken(String token) {
        return "ACCESS".equals(
                extractAllClaims(token)
                        .get("tokenType", String.class)
        );
    }

    private Claims extractAllClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

}