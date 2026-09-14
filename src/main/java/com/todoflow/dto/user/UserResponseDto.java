package com.todoflow.dto.user;

import com.todoflow.enums.Role;

import java.time.Instant;

public record UserResponseDto(
        Long id,
        String username,
        String email,
        Role role,
        boolean enabled,
        Instant createdAt,
        Instant updatedAt
) {
}