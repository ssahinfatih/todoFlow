package com.todoflow.service;

import com.todoflow.dto.todoShare.TodoShareRequestDto;
import com.todoflow.dto.todoShare.TodoShareResponseDto;

import java.util.List;

public interface ITodoShareService {

    TodoShareResponseDto shareTodo(
            TodoShareRequestDto request
    );

    List<TodoShareResponseDto> getSharedTodos();

    void deleteShare(Long id);
}