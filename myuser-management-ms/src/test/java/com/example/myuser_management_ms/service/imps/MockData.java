package com.example.myuser_management_ms.service.imps;

import com.example.myuser_management_ms.dto.req.UserRequestDto;
import com.example.myuser_management_ms.dto.res.UserResponseDto;
import com.example.myuser_management_ms.entity.UserEntity;
import com.example.myuser_management_ms.utils.UserRole;

public class MockData {

    public static UserRequestDto userRequestDto() {
        return UserRequestDto.builder()
                .role(UserRole.USER)
                .reqName("test")
                .password("134677")
                .surname("test")
                .build();
    }

    public static UserEntity userEntity() {
        return UserEntity.builder()
                .name("test")
                .surname("test")
                .role(UserRole.USER)
                .build();
    }

    public static UserResponseDto userResponseDto() {
        return UserResponseDto.builder()
                .email("test")
                .userName("test")
                .build();
    }
}
