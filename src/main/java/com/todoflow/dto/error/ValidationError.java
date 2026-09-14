package com.todoflow.dto.error;

public record ValidationError(
        String field,
        String message
) {
}