package com.todoflow.dto.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoUpdateRequestDto(

        @NotBlank(message = "Başlık boş bırakılamaz")
        @Size(max = 200, message = "Başlık en fazla 200 karakter olabilir")
        String title,

        @Size(max = 1000, message = "Açıklama en fazla 1000 karakter olabilir")
        String description

) {
}