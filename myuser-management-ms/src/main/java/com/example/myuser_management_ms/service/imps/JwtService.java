package com.example.myuser_management_ms.service.imps;

import com.example.myuser_management_ms.config.JwtProperties;
import com.example.myuser_management_ms.entity.RefreshTokenEntity;
import com.example.myuser_management_ms.entity.UserEntity;
import com.example.myuser_management_ms.repository.IRefreshTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.SecureRandom;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;
    private Key signingKey;;
    private final IRefreshTokenRepository refreshTokenRepository;

    @PostConstruct
    void check() {
        System.out.println("jwtProperties" + jwtProperties);
    }

    public JwtService(JwtProperties jwtProperties, IRefreshTokenRepository refreshTokenRepository) {
        this.jwtProperties = jwtProperties;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @PostConstruct
    void init() {
        this.signingKey = Keys.hmacShaKeyFor(
                jwtProperties.accessToken().secret().getBytes()
        );
    }

    public String generateAccessToken(UserEntity userEntity) {
        Instant now = new Date().toInstant();
        Instant expiry =
                now.plus(jwtProperties.accessToken()
                        .expirationMinutes(),
                        ChronoUnit.MINUTES);

        return Jwts.builder()
                .setSubject(userEntity.getId())
                .claim("userName", userEntity.getUserName())
                .claim("role", userEntity.getRole())
                .claim("email", userEntity.getEmail())
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(expiry))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public RefreshTokenEntity createRefreshToken(UserEntity userEntity) {
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime expiresAt = now.plus(
                jwtProperties.refreshToken().expirationDays(),
                ChronoUnit.DAYS
        );

        String tokenValue = generateSecureRandomToken(64);

        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .user(userEntity)
                .token(tokenValue)
                .expiresAt(expiresAt)
                .revoked(false)
                .createdAt(now)
                .build();

        return refreshTokenRepository.save(refreshToken);
    }

    private String generateSecureRandomToken(int length) {
        SecureRandom secureRandom = new SecureRandom();
        byte[] tokenBytes = new byte[length];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }
}
