package ru.school.tournamenthub.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long jwtExpirationMs;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String username, String role) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpirationMs);

        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String getUsernameFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.getSubject();
        } catch (ExpiredJwtException ex) {
            return ex.getClaims().getSubject();
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Ошибка извлечения username из токена: {}", ex.getMessage());
            return null;
        }
    }

    public String getRoleFromToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
            return claims.get("role", String.class);
        } catch (ExpiredJwtException ex) {
            return ex.getClaims().get("role", String.class);
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Ошибка извлечения роли из токена: {}", ex.getMessage());
            return null;
        }
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (SecurityException ex) {
            log.error("Неверная подпись JWT");
        } catch (MalformedJwtException ex) {
            log.error("Неверный формат JWT токена");
        } catch (ExpiredJwtException ex) {
            log.warn("Срок действия JWT токена истек");
        } catch (UnsupportedJwtException ex) {
            log.error("Неподдерживаемый JWT токен");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims строка пуста");
        }
        return false;
    }

    public TokenInfo getTokenInfo(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            return new TokenInfo(
                    claims.getSubject(),
                    claims.get("role", String.class),
                    claims.getIssuedAt(),
                    claims.getExpiration(),
                    false
            );
        } catch (ExpiredJwtException ex) {
            Claims claims = ex.getClaims();
            return new TokenInfo(
                    claims.getSubject(),
                    claims.get("role", String.class),
                    claims.getIssuedAt(),
                    claims.getExpiration(),
                    true
            );
        } catch (JwtException | IllegalArgumentException ex) {
            log.error("Ошибка получения информации о токене: {}", ex.getMessage());
            return null;
        }
    }

    public static class TokenInfo {
        private final String username;
        private final String role;
        private final Date issuedAt;
        private final Date expiration;
        private final boolean expired;

        public TokenInfo(String username, String role, Date issuedAt, Date expiration, boolean expired) {
            this.username = username;
            this.role = role;
            this.issuedAt = issuedAt;
            this.expiration = expiration;
            this.expired = expired;
        }

        public String getUsername() {
            return username;
        }

        public String getRole() {
            return role;
        }

        public Date getIssuedAt() {
            return issuedAt;
        }

        public Date getExpiration() {
            return expiration;
        }

        public boolean isExpired() {
            return expired;
        }
    }
}