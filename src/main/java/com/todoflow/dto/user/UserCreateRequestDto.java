package com.todoflow.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequestDto(

        @NotBlank(message = "Kullanıcı adı boş bırakılamaz")
        @Size(min = 3, max = 50, message = "Kullanıcı adı 3-50 karakter arasında olmalıdır")
        String username,

        @NotBlank(message = "Email boş bırakılamaz")
        @Email(message = "Geçerli bir email adresi giriniz")
        String email,

        @NotBlank(message = "Şifre boş bırakılamaz")
        @Size(min = 6, max = 100, message = "Şifre en az 6 karakter olmalıdır")
        String password
) {
}