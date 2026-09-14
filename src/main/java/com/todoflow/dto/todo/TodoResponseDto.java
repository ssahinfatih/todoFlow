package com.todoflow.dto.todo;

import java.time.Instant;

public record TodoResponseDto(
        Long id,
        String title,
        String description,
        boolean completed,
        Long ownerId,
        Instant createdAt,
        Instant updatedAt
) {
}