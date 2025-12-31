package com.example.be.security.jwt;

import com.example.be.user.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Component
public class JwtProvider {

  private final SecretKey secretKey;
  private final long accessTokenExpiration;
  private final long refreshTokenExpiration;

  public JwtProvider(
      @Value("${jwt.secret}") String secret,
      @Value("${jwt.access-token-expiration}") long accessTokenExpiration,
      @Value("${jwt.refresh-token-expiration}") long refreshTokenExpiration) {
    this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    this.accessTokenExpiration = accessTokenExpiration;
    this.refreshTokenExpiration = refreshTokenExpiration;
  }

  public String generateAccessToken(Long userId, Role role) {
    return Jwts.builder()
        .subject(String.valueOf(userId))
        .claim("type", "access")
        .claim("role", role.getRole())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + accessTokenExpiration))
        .signWith(secretKey)
        .compact();
  }

  public String generateRefreshToken(Long userId, Role role) {
    return Jwts.builder()
        .subject(String.valueOf(userId))
        .claim("type", "refresh")
        .claim("role", role.getRole())
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + refreshTokenExpiration))
        .signWith(secretKey)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  public Long getUserId(String token) {
    Claims claims = getClaims(token);

    return Long.parseLong(claims.getSubject());
  }

  public Role getRole(String token) {
    Claims claims = getClaims(token);
    String roleName = claims.get("role", String.class);

    return Role.valueOf(roleName);
  }

  private Claims getClaims(String token) {
    return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
  }

  public Long getExpiration(String token) {
    Claims claims = getClaims(token);

    return claims.getExpiration().getTime();
  }

  public Authentication getAuthentication(String token) {
    Long userId = getUserId(token);
    String role = getRole(token).getRole();

    return new UsernamePasswordAuthenticationToken(
        userId, null, List.of(new SimpleGrantedAuthority("ROLE_" + role)));
  }
}
