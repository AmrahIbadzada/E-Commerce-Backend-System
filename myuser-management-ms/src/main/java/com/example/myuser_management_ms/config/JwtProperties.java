package com.example.myuser_management_ms.config;

import com.example.myuser_management_ms.dto.AccessTokenProperties;
import com.example.myuser_management_ms.dto.RefreshTokenProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(@NotNull
                            @Valid
                            AccessTokenProperties accessToken,

                            @NotNull
                            @Valid
                            RefreshTokenProperties refreshToken) {
}
