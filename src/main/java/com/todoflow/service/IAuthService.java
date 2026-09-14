package com.todoflow.service;


import com.todoflow.dto.auth.LoginRequestDto;
import com.todoflow.dto.auth.RefreshTokenRequestDto;
import com.todoflow.dto.auth.TokenPairDto;
import com.todoflow.dto.user.UserCreateRequestDto;
import com.todoflow.dto.user.UserResponseDto;

public interface IAuthService {
    UserResponseDto register(UserCreateRequestDto request);
    TokenPairDto login(LoginRequestDto request);
    TokenPairDto refresh(RefreshTokenRequestDto request);
    void logout(RefreshTokenRequestDto request);
}
