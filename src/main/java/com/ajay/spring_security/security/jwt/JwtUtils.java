package com.ajay.spring_security.security.jwt;

import com.ajay.spring_security.security.services.UserDetailsImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtUtils {

    @Value("${app.security.jwt.secret}")
    private String jwtSecret;
    @Value("${app.security.jwt.expiration}")
    private int jwtExpiration;


    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();
        long expirationTime = System.currentTimeMillis() + jwtExpiration;
        List<String> authorities = new ArrayList<>();
        for (GrantedAuthority authority : userPrincipal.getAuthorities()) {
            authorities.add(authority.getAuthority());
        }

        return Jwts.builder()
                .claim("username", userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .claim("email", userPrincipal.getEmail())
                .claim("authorities", authorities)
                .issuedAt(new Date())
                .expiration(new Date(expirationTime))
                .signWith(key())
                .compact();
    }


    private SecretKey key() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().verifyWith(key()).build()
                .parseSignedClaims(token).getPayload().get("authorities", String.class);
    }

    public List<SimpleGrantedAuthority> getAuthoritiesFromJwtToken(String token) {
        List<?> rawAuthorities = Jwts.parser().verifyWith(key()).build()
                .parseSignedClaims(token).getPayload().get("authorities", List.class);
        List<String> authorities = new ArrayList<>();
        for (Object authority : rawAuthorities) {
            if (authority instanceof String) {
                authorities.add((String) authority);
            } else {
                throw new IllegalArgumentException("Invalid authority value in token");
            }
        }
        return authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .toList();
    }


    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }

        return false;
    }
}
