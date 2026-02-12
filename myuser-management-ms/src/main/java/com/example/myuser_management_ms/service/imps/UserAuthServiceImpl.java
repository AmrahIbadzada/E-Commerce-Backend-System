package com.example.myuser_management_ms.service.imps;

import com.example.myuser_management_ms.config.PasswordEncoderConfig;
import com.example.myuser_management_ms.dto.req.LoginRequest;
import com.example.myuser_management_ms.dto.res.AuthResponse;
import com.example.myuser_management_ms.entity.RefreshTokenEntity;
import com.example.myuser_management_ms.entity.UserEntity;
import com.example.myuser_management_ms.exception.UserNotFoundException;
import com.example.myuser_management_ms.exception.WrongPasswordException;
import com.example.myuser_management_ms.repository.IUserRepository;
import com.example.myuser_management_ms.service.IUserAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAuthServiceImpl implements IUserAuthService {

    private final IUserRepository userRepository;
    private final PasswordEncoderConfig passwordEncoderConfig;
    private final JwtService jwtService;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        UserEntity userEntity = userRepository.findByUserName(loginRequest.userName());

        if (userEntity == null) {
            throw new UserNotFoundException("User not found");
        }

        boolean isValid = passwordEncoderConfig.passwordEncoder().matches(loginRequest.password(), userEntity.getPassword());

        if (!isValid) {
            throw new WrongPasswordException("Wrong password");
        }

        String accessToken = jwtService.generateAccessToken(userEntity);
        RefreshTokenEntity refreshToken = jwtService.createRefreshToken(userEntity);

        return new  AuthResponse(
                accessToken,
                refreshToken.getToken());
    }
}
