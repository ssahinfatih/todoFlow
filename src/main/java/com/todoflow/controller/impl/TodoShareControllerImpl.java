package com.todoflow.controller.impl;

import com.todoflow.controller.ITodoShareController;
import com.todoflow.dto.todoShare.TodoShareRequestDto;
import com.todoflow.dto.todoShare.TodoShareResponseDto;
import com.todoflow.service.ITodoShareService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todo-shares")
public class TodoShareControllerImpl
        implements ITodoShareController {

    private final ITodoShareService todoShareService;

    public TodoShareControllerImpl(
            ITodoShareService todoShareService
    ) {
        this.todoShareService = todoShareService;
    }

    @Override
    @PostMapping("/share")
    public ResponseEntity<TodoShareResponseDto> shareTodo(
            @Valid @RequestBody TodoShareRequestDto request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(todoShareService.shareTodo(request));
    }

    @Override
    @GetMapping("/all")
    public ResponseEntity<List<TodoShareResponseDto>> getSharedTodos() {

        return ResponseEntity.ok(
                todoShareService.getSharedTodos()
        );
    }

    @Override
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteShare(
            @PathVariable Long id
    ) {

        todoShareService.deleteShare(id);

        return ResponseEntity.noContent().build();
    }
}