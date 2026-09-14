package com.todoflow.dto.todoShare;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TodoShareRequestDto(

        @NotNull(message = "Todo id boş bırakılamaz")
        Long todoId,

        @NotBlank(message = "Kullanıcı adı boş bırakılamaz")
        String username
) {
}