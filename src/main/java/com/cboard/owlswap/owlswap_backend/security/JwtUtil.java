package com.cboard.owlswap.owlswap_backend.security;

import com.cboard.owlswap.owlswap_backend.model.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {
    private final String SECRET_KEY = "supersecuresecretkeythatis32chars!";
    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    private final long EXPIRATION_TIME = 86400000; // 24 hours

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername()) //unique user identity
                .claim("id", user.getUserId()) // user id
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    public Integer extractUserId(String token)
    {
        /*Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();*/

        Claims claims = extractAllClaims(token);

        Object id = claims.get("id");
        if (id instanceof Integer i)
            return i;
        if (id instanceof Number n)
            return n.intValue();
        if (id instanceof String s)
            return Integer.parseInt(s);

        throw new JwtException("Invalid is claim type");

    }

    private Claims extractAllClaims(String token)
    {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}