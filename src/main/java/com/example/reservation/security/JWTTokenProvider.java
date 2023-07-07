package com.example.reservation.security;

import com.example.reservation.models.ReservationAPIException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JWTTokenProvider {
    @Value("${app.jwt-secret}")
    private String jwtSecretKey;

    @Value("${app-jwt-expiration-milliseconds}")
    private Long expirationTime;

    public String generateToken(Authentication authentication) {
        String userName = authentication.getName();
        Date expirationDate = new Date(new Date().getTime() + expirationTime);
        return Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(expirationDate)
                .signWith(key())
                .compact();
    }

    public String getUserName(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new ReservationAPIException("Invalid Token", HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            throw new ReservationAPIException("Expired Token", HttpStatus.UNAUTHORIZED);
        } catch (UnsupportedJwtException e) {
            throw new ReservationAPIException("Unsupported Token", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            throw new ReservationAPIException("Invalid Arguments", HttpStatus.BAD_REQUEST);
        }
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecretKey));
    }
}
