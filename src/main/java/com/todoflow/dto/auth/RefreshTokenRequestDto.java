package com.todoflow.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenRequestDto(

        @NotBlank(message = "Refresh token boş bırakılamaz")
        String refreshToken
) {
}