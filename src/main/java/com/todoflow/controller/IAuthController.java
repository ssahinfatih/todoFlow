package com.todoflow.controller;

import com.todoflow.dto.auth.AccessTokenResponseDto;
import com.todoflow.dto.auth.LoginRequestDto;
import com.todoflow.dto.auth.RefreshTokenRequestDto;
import com.todoflow.dto.auth.TokenPairDto;
import com.todoflow.dto.user.UserCreateRequestDto;
import com.todoflow.dto.user.UserResponseDto;
import org.springframework.http.ResponseEntity;

public interface IAuthController {

    ResponseEntity<UserResponseDto> register(UserCreateRequestDto request);

    ResponseEntity<TokenPairDto> login(LoginRequestDto request);

    ResponseEntity<TokenPairDto> refresh(RefreshTokenRequestDto request);

    ResponseEntity<Void> logout(RefreshTokenRequestDto request);
}