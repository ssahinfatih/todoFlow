package com.todoflow.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @NotBlank(message = "Kullanıcı adı boş bırakılamaz")
        String username,

        @NotBlank(message = "Şifre boş bırakılamaz")
        String password
) {
}