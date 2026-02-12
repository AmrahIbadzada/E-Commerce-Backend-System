package com.example.myuser_management_ms.dto.res;

import com.example.myuser_management_ms.utils.UserRole;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserUpdateResponseDto(
        String name,
        String surname,
        String userName,
        String email,
        String password,
        UserRole role,
        String phoneNumber,
        Instant birthDate
) {
}
