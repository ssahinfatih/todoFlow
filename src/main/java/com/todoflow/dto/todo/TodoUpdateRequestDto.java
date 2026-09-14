package com.todoflow.dto.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TodoUpdateRequestDto(

        @NotBlank(message = "Başlık boş bırakılamaz")
        @Size(max = 150, message = "Başlık 150 karakterden fazla olamaz")
        String title,

        @Size(max = 2000, message = "Açıklama 2000 karakterden fazla olamaz")
        String description,

        boolean completed

) {
}