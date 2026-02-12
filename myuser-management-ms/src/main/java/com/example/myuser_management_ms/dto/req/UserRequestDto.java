package com.example.myuser_management_ms.dto.req;

import com.example.myuser_management_ms.utils.UserRole;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

import java.time.Instant;

@Builder
public record UserRequestDto(
        @NotBlank(message = "reqName bos ola bilmez!")
        String reqName,
        String surname,
        @NotBlank(message = "userName bos ola bilmez!")
        String userName,
        String email,
        @NotBlank(message = "password bos ola bilmez!")
        String password,
        UserRole role,
        String phoneNumber,
        Instant birthDate
) {
}
