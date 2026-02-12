package com.example.myuser_management_ms.service;

import com.example.myuser_management_ms.dto.req.UserRequestDto;
import com.example.myuser_management_ms.dto.req.UserUpdateRequestDto;
import com.example.myuser_management_ms.dto.res.UserResponseDto;
import com.example.myuser_management_ms.dto.res.UserUpdateResponseDto;

import java.util.List;

public interface IUserService {

    UserResponseDto createUser(UserRequestDto userRequestDto);

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUsers(String userName);

    UserUpdateResponseDto updateUsers(UserUpdateRequestDto userRequestDto,
                                      String userName);

    void deleteUsers(String userName);
}
