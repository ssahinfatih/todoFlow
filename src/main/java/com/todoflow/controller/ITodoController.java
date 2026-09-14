package com.todoflow.controller;

import com.todoflow.dto.todo.TodoCreateRequestDto;
import com.todoflow.dto.todo.TodoResponseDto;
import com.todoflow.dto.todo.TodoUpdateRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ITodoController {

    ResponseEntity<TodoResponseDto> createTodo(
            TodoCreateRequestDto request
    );

    ResponseEntity<List<TodoResponseDto>> getAllTodos();

    ResponseEntity<TodoResponseDto> getTodoById(Long id);

    ResponseEntity<TodoResponseDto> updateTodo(
            Long id,
            TodoUpdateRequestDto request
    );

    ResponseEntity<Void> deleteTodo(Long id);
    ResponseEntity<TodoResponseDto> updateCompleted(
            Long id,
            boolean completed
    );
    ResponseEntity<List<TodoResponseDto>> getTodosByCompleted(
            boolean completed
    );
}