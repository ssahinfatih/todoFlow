package com.todoflow.controller.impl;

import com.todoflow.controller.IAuthController;
import com.todoflow.dto.auth.AccessTokenResponseDto;
import com.todoflow.dto.auth.LoginRequestDto;
import com.todoflow.dto.auth.RefreshTokenRequestDto;
import com.todoflow.dto.auth.TokenPairDto;
import com.todoflow.dto.user.UserCreateRequestDto;
import com.todoflow.dto.user.UserResponseDto;
import com.todoflow.service.IAuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthControllerImpl implements IAuthController {

    private final IAuthService authService;


    @Override
    @PostMapping("/register")
    public ResponseEntity<UserResponseDto> register(
            @Valid @RequestBody UserCreateRequestDto request
    ) {

        UserResponseDto response = authService.register(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @Override
    @PostMapping("/login")
    public ResponseEntity<TokenPairDto> login(
            @Valid @RequestBody LoginRequestDto request
    ) {

        TokenPairDto response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/refresh")
    public ResponseEntity<TokenPairDto> refresh(@Valid @RequestBody RefreshTokenRequestDto request) {
        TokenPairDto response = authService.refresh(request);
        return ResponseEntity.ok(response);
    }

    @Override
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@Valid @RequestBody RefreshTokenRequestDto request) {
        authService.logout(request);

        return ResponseEntity.noContent().build();
    }

}
