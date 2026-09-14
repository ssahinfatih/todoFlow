package com.todoflow.controller.impl;

import com.todoflow.controller.ITodoController;
import com.todoflow.dto.todo.TodoCreateRequestDto;
import com.todoflow.dto.todo.TodoResponseDto;
import com.todoflow.dto.todo.TodoUpdateRequestDto;
import com.todoflow.service.ITodoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
public class TodoControllerImpl implements ITodoController {

    private final ITodoService todoService;

    public TodoControllerImpl(ITodoService todoService) {
        this.todoService = todoService;
    }

    @Override
    @PostMapping("/create")
    public ResponseEntity<TodoResponseDto> createTodo(
            @Valid @RequestBody TodoCreateRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(todoService.createTodo(request));
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<TodoResponseDto>> getAllTodos() {

        return ResponseEntity.ok(
                todoService.getAllTodos()
        );
    }

    @Override
    @GetMapping("/getbyid/{id}")
    public ResponseEntity<TodoResponseDto> getTodoById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                todoService.getTodoById(id)
        );
    }

    @Override
    @PutMapping("/update/{id}")
    public ResponseEntity<TodoResponseDto> updateTodo(
            @PathVariable Long id,
            @Valid @RequestBody TodoUpdateRequestDto request
    ) {

        return ResponseEntity.ok(
                todoService.updateTodo(id, request)
        );
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTodo(
            @PathVariable Long id
    ) {

        todoService.deleteTodo(id);

        return ResponseEntity.noContent().build();
    }
    @Override
    @PatchMapping("/complete/{id}")
    public ResponseEntity<TodoResponseDto> updateCompleted(
            @PathVariable Long id,
            @RequestParam boolean completed
    ) {

        return ResponseEntity.ok(
                todoService.updateCompleted(
                        id,
                        completed
                )
        );
    }
    @Override
    @GetMapping("/completed")
    public ResponseEntity<List<TodoResponseDto>> getTodosByCompleted(
            @RequestParam boolean completed
    ) {

        return ResponseEntity.ok(
                todoService.getTodosByCompleted(completed)
        );
    }
}