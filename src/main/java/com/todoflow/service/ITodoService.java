package com.todoflow.service;

import com.todoflow.dto.todo.TodoCreateRequestDto;
import com.todoflow.dto.todo.TodoResponseDto;
import com.todoflow.dto.todo.TodoUpdateRequestDto;

import java.util.List;

public interface ITodoService {

    TodoResponseDto createTodo(TodoCreateRequestDto request);

    List<TodoResponseDto> getAllTodos();

    TodoResponseDto getTodoById(Long id);

    TodoResponseDto updateTodo(
            Long id,
            TodoUpdateRequestDto request
    );

    void deleteTodo(Long id);
    TodoResponseDto updateCompleted(
            Long id,
            boolean completed
    );
    List<TodoResponseDto> getTodosByCompleted(boolean completed);
}