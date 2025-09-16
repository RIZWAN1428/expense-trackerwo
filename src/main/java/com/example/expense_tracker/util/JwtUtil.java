package com.example.expense_tracker.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.security.Key;

@Component
public class JwtUtil {

    private final Key key = Keys.hmacShaKeyFor("mysecretkeymysecretkeymysecretkeymysecretkey".getBytes());

    public String generateToken(String username, String role, boolean canDelete, boolean canEdit) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .claim("canDelete", canDelete)
                .claim("canEdit", canEdit)
                .setIssuer("expense-tracker")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String extractRole(String token) {
        return (String) Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role");
    }

    public boolean extractCanDelete(String token) {
        return Boolean.TRUE.equals(
                Jwts.parserBuilder().setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .get("canDelete", Boolean.class)
        );
    }

    public boolean extractCanEdit(String token) {
        return Boolean.TRUE.equals(
                Jwts.parserBuilder().setSigningKey(key)
                        .build()
                        .parseClaimsJws(token)
                        .getBody()
                        .get("canEdit", Boolean.class)
        );
    }
}
