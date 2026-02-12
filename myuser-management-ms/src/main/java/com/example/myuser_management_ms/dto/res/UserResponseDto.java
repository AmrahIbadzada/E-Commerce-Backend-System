package com.example.myuser_management_ms.dto.res;

import lombok.Builder;

@Builder
public record UserResponseDto(
        String userName,
        String email
) {
}
