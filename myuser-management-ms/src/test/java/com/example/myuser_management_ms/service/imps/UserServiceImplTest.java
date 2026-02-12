package com.example.myuser_management_ms.service.imps;

import com.example.myuser_management_ms.config.PasswordEncoderConfig;
import com.example.myuser_management_ms.dto.req.UserRequestDto;
import com.example.myuser_management_ms.dto.res.UserResponseDto;
import com.example.myuser_management_ms.entity.UserEntity;
import com.example.myuser_management_ms.mapper.IUserMapper;
import com.example.myuser_management_ms.repository.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.mockito.Mockito.*;

@SpringBootTest(classes = {
        UserServiceImpl.class, IUserRepository.class, IUserMapper.class, PasswordEncoderConfig.class
})
class UserServiceImplTest {

    @MockitoBean
    IUserRepository userRepository;
    @MockitoBean
    IUserMapper userMapper;
    @Spy
    PasswordEncoderConfig passwordEncoderConfig;

    @Autowired
    UserServiceImpl userService;

    UserRequestDto userRequestDto;

    UserEntity userEntity;

    UserResponseDto userResponseDto;


    @BeforeEach
    void setUp() {

        userRequestDto = MockData.userRequestDto();
        userEntity = MockData.userEntity();
        userResponseDto = MockData.userResponseDto();
    }

    @Test
    void testWhenUserIsCreated() {

        //when
        when(userMapper.mapRequestDtoToEntity(userRequestDto)).thenReturn(userEntity);
        when(userMapper.mapEntityToResponseDto(userEntity)).thenReturn(userResponseDto);


        //then
        userService.createUser(userRequestDto);

        //verify
        verify(userMapper).mapRequestDtoToEntity(userRequestDto);
        verify(userRepository, times(2)).save(userEntity);
        verify(userMapper).mapEntityToResponseDto(userEntity);
    }
}