package com.example.myuser_management_ms.service;

import com.example.myuser_management_ms.dto.req.LoginRequest;
import com.example.myuser_management_ms.dto.res.AuthResponse;

public interface IUserAuthService {

    AuthResponse login(LoginRequest loginRequest);
}
