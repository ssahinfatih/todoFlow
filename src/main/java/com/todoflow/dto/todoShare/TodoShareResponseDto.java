package com.todoflow.dto.todoShare;

import java.time.Instant;

public record TodoShareResponseDto(
        Long id,
        Long todoId,
        Long ownerId,
        String ownerUsername,
        Long userId,
        String username,
        Instant sharedAt
) {
}