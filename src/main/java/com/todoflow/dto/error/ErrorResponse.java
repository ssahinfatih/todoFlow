package com.todoflow.dto.error;

import java.time.Instant;
import java.util.List;

public record ErrorResponse(
        String message,
        int statusCode,
        String path,
        Instant timestamp,
        List<ValidationError> errors
) {
}