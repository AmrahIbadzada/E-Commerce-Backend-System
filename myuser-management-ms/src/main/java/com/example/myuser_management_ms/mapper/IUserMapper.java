package com.example.myuser_management_ms.mapper;

import com.example.myuser_management_ms.dto.req.UserRequestDto;
import com.example.myuser_management_ms.dto.req.UserUpdateRequestDto;
import com.example.myuser_management_ms.dto.res.UserResponseDto;
import com.example.myuser_management_ms.dto.res.UserUpdateResponseDto;
import com.example.myuser_management_ms.entity.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IUserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "name", source = "reqName")
    UserEntity mapRequestDtoToEntity(UserRequestDto userRequestDto);

    UserResponseDto mapEntityToResponseDto(UserEntity userEntity);

    List<UserResponseDto> mapEntityListToResponseDtoList(List<UserEntity> userEntities);

    UserUpdateResponseDto mapEntityToUpdateResponseDto(UserEntity userEntity);

    UserEntity mapUpdateRequestDtoToEntity(UserUpdateRequestDto userRequestDto,
                                           String userName);
}
