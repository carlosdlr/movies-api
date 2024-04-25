package com.backbase.movies.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class JWTService {

    private final SecretKey key;
    private final JwtParser parser;

    public JWTService() {
        this.key = Keys.hmacShaKeyFor("abcdefghijklmnopqrstuvwxyz1234567890".getBytes());
        this.parser = Jwts.parserBuilder().setSigningKey(this.key).build();
    }

    public String generateToken(String username) {
        JwtBuilder builder = Jwts.builder()
                .setSubject(username)
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)))
                .signWith(this.key);
        return builder.compact();
    }

    public String getUsername(String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public boolean isValidateToken(UserDetails user, String token) {
        Claims claims = parser.parseClaimsJws(token).getBody();
        boolean isExpired = claims.getExpiration().after(Date.from(Instant.now()));
        return isExpired && user.getUsername().equals(claims.getSubject());
    }
}
