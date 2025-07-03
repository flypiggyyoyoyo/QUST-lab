package com.flypiggyyoyoyo.backend.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretBase64;  // 存储Base64编码的密钥

    @Value("${jwt.access-expiration}")
    private long accessExpiration;

    @Value("${jwt.refresh-expiration}")
    private long refreshExpiration;

    // 解码Base64密钥并生成SecretKey（仅初始化一次）
    private SecretKey getSecretKey() {
        byte[] keyBytes = Base64.getDecoder().decode(secretBase64);
        return Keys.hmacShaKeyFor(keyBytes);  // 生成符合HS512要求的密钥
    }

    /**
     * 生成Access Token
     */
    public String generateAccessToken(Integer userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        return doGenerateToken(claims, userId, accessExpiration);
    }

    /**
     * 生成Refresh Token
     */
    public String generateRefreshToken(Integer userId, String username) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        return doGenerateToken(claims, userId, refreshExpiration);
    }

    /**
     * 生成Token的通用方法
     */
    private String doGenerateToken(Map<String, Object> claims, Integer userId, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userId.toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(getSecretKey(), SignatureAlgorithm.HS512)  // 使用SecretKey签名
                .compact();
    }

    /**
     * 从Token中获取用户ID
     */
    public Integer getUserIdFromToken(String token) {
        String userId = getClaimsFromToken(token).getSubject();
        return Integer.parseInt(userId);
    }

    /**
     * 从Token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        return getClaimsFromToken(token).get("username", String.class);
    }

    /**
     * 从Token中获取Claims
     */
    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()  // 使用新的parserBuilder()方法
                .setSigningKey(getSecretKey())  // 使用SecretKey验证签名
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 验证Token有效性
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()  // 使用新的parserBuilder()方法
                    .setSigningKey(getSecretKey())  // 使用SecretKey验证签名
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | ExpiredJwtException |
                 UnsupportedJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * 判断Token是否过期
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getClaimsFromToken(token).getExpiration();
        return expiration.before(new Date());
    }
}