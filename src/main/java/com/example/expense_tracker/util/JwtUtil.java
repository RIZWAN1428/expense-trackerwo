package com.example.expense_tracker.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.security.Key;

@Component //Makes it a Spring bean.
public class JwtUtil {

    private final Key key = Keys.hmacShaKeyFor("mysecretkeymysecretkeymysecretkeymysecretkey".getBytes());

    public String generateToken(String username) {
        //Creates a JWT with username, issuer, time, and 1-day expiry.
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("expense-tracker")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 1 day
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        //Parses token and gets the username from it.
        return Jwts.parserBuilder().setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
