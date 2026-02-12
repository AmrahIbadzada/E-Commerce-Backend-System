package com.example.myuser_management_ms.dto.req;

import com.example.myuser_management_ms.utils.UserRole;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.Instant;

public record UserUpdateRequestDto(
        @NotBlank(message = "reqName bos ola bilmez!")
        String name,
        @Size(min = 3, max = 10, message = "min olcuden daha az simvol yazilib!")
        String surname,
        String email,
        @Min(value = 8, message = "parol en az 8 simvoldan ibaret olmalidir!")
        String password,
        UserRole role,
        String phoneNumber,
        Instant birthDate
) {
}
