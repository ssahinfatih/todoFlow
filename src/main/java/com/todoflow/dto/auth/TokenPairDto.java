package com.todoflow.dto.auth;

public record TokenPairDto(
        String accessToken,
        String refreshToken
) {
}