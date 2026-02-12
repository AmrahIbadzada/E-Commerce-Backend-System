package com.example.myuser_management_ms.service.imps;

import com.example.myuser_management_ms.config.PasswordEncoderConfig;
import com.example.myuser_management_ms.dto.req.UserRequestDto;
import com.example.myuser_management_ms.dto.req.UserUpdateRequestDto;
import com.example.myuser_management_ms.dto.res.UserResponseDto;
import com.example.myuser_management_ms.dto.res.UserUpdateResponseDto;
import com.example.myuser_management_ms.entity.UserEntity;
import com.example.myuser_management_ms.exception.UserNotFoundException;
import com.example.myuser_management_ms.mapper.IUserMapper;
import com.example.myuser_management_ms.repository.IUserRepository;
import com.example.myuser_management_ms.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {

    IUserRepository userRepository;
    IUserMapper userMapper;
    PasswordEncoderConfig passwordEncoderConfig;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        UserEntity userEntity = userMapper.mapRequestDtoToEntity(userRequestDto);
        userEntity.setPassword(passwordEncoderConfig.passwordEncoder()
                .encode(userRequestDto.password()));
        userRepository.save(userEntity);
        userRepository.save(userEntity);

        return userMapper.mapEntityToResponseDto(userEntity);
    }

    @Override
    public List<UserResponseDto> getAllUsers() {
        List<UserEntity> userEntities = userRepository.findAll();

        return userMapper.mapEntityListToResponseDtoList(userEntities);
    }

    @Override
    public UserResponseDto getUsers(String userName) {

        UserEntity userEntity = userRepository.findByUserName(userName);
        if (userEntity == null) {
            throw new UserNotFoundException("User not found");
        }
        return userMapper.mapEntityToResponseDto(userEntity);
    }

    @Override
    public UserUpdateResponseDto updateUsers(UserUpdateRequestDto userRequestDto, String userName) {
        UserEntity userEntity = userRepository.findByUserName(userName);
        if (userEntity == null) {
            throw new UserNotFoundException("User not found");
        }

        userEntity.setName(userRequestDto.name());
        userEntity.setSurname(userRequestDto.surname());
        userEntity.setEmail(userRequestDto.email());
        userEntity.setPassword(userRequestDto.password());
        userEntity.setRole(userRequestDto.role());
        userEntity.setBirthDate(userRequestDto.birthDate());
        userEntity.setUpdatedAt(LocalDateTime.now());

        userRepository.save(userEntity);

        return UserUpdateResponseDto.builder()
                .name(userEntity.getName())
                .surname(userEntity.getSurname())
                .email(userEntity.getEmail())
                .phoneNumber(userEntity.getPhoneNumber())
                .birthDate(userEntity.getBirthDate())
                .role(userEntity.getRole())
                .build();
    }

    @Override
    @Transactional
    public void deleteUsers(String userName) {
        UserEntity user = userRepository.findByUserName(userName);

        if (user == null) {
            throw new UserNotFoundException("User not found");
        }
        userRepository.deleteByUserName(userName);
    }
}
