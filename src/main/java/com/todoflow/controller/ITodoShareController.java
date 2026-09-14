package com.todoflow.controller;

import com.todoflow.dto.todoShare.TodoShareRequestDto;
import com.todoflow.dto.todoShare.TodoShareResponseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITodoShareController {

    ResponseEntity<TodoShareResponseDto> shareTodo(
            TodoShareRequestDto request
    );

    ResponseEntity<List<TodoShareResponseDto>> getSharedTodos();

    ResponseEntity<Void> deleteShare(Long id);
}