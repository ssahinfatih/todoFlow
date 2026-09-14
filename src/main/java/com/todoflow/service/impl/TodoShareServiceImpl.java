package com.todoflow.service.impl;

import com.todoflow.dto.todoShare.TodoShareRequestDto;
import com.todoflow.dto.todoShare.TodoShareResponseDto;
import com.todoflow.entity.Todo;
import com.todoflow.entity.TodoShare;
import com.todoflow.entity.User;
import com.todoflow.exception.BadRequestException;
import com.todoflow.exception.NotFoundException;
import com.todoflow.repository.TodoRepository;
import com.todoflow.repository.TodoShareRepository;
import com.todoflow.service.ITodoShareService;
import com.todoflow.service.IUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TodoShareServiceImpl implements ITodoShareService {

    private final TodoShareRepository todoShareRepository;
    private final TodoRepository todoRepository;
    private final IUserService userService;

    public TodoShareServiceImpl(
            TodoShareRepository todoShareRepository,
            TodoRepository todoRepository,
            IUserService userService
    ) {
        this.todoShareRepository = todoShareRepository;
        this.todoRepository = todoRepository;
        this.userService = userService;
    }

    @Override
    public TodoShareResponseDto shareTodo(
            TodoShareRequestDto request
    ) {

        String currentUsername = getUsername();

        Todo todo = todoRepository
                .findByIdAndOwnerUsername(
                        request.todoId(),
                        currentUsername
                )
                .orElseThrow(() ->
                        new NotFoundException(
                                "Todo bulunamadı veya size ait değil"
                        )
                );

        User sharedUser = userService
                .findByUsername(request.username())
                .orElseThrow(() ->
                        new NotFoundException(
                                "Paylaşılacak kullanıcı bulunamadı"
                        )
                );

        if (currentUsername.equals(sharedUser.getUsername())) {
            throw new BadRequestException(
                    "Todo kendinizle paylaşılamaz"
            );
        }

        if (todoShareRepository.existsByTodoIdAndUserId(
                todo.getId(),
                sharedUser.getId()
        )) {
            throw new BadRequestException(
                    "Todo bu kullanıcıyla zaten paylaşılmış"
            );
        }

        TodoShare todoShare =
                new TodoShare(
                        todo,
                        sharedUser
                );

        TodoShare savedShare =
                todoShareRepository.save(todoShare);

        return toResponseDto(savedShare);
    }

    @Override
    public List<TodoShareResponseDto> getSharedTodos() {

        String username = getUsername();

        return todoShareRepository
                .findAllByUserUsername(username)
                .stream()
                .map(this::toResponseDto)
                .toList();
    }

    @Override
    public void deleteShare(Long id) {

        String username = getUsername();

        TodoShare todoShare =
                todoShareRepository.findById(id)
                        .orElseThrow(() ->
                                new NotFoundException(
                                        "Todo paylaşımı bulunamadı"
                                )
                        );

        if (!todoShare
                .getTodo()
                .getOwner()
                .getUsername()
                .equals(username)) {

            throw new NotFoundException(
                    "Todo paylaşımı bulunamadı"
            );
        }

        todoShareRepository.delete(todoShare);
    }

    private String getUsername() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    private TodoShareResponseDto toResponseDto(
            TodoShare todoShare
    ) {

        return new TodoShareResponseDto(
                todoShare.getId(),
                todoShare.getTodo().getId(),

                todoShare.getTodo().getOwner().getId(),
                todoShare.getTodo().getOwner().getUsername(),

                todoShare.getUser().getId(),
                todoShare.getUser().getUsername(),

                todoShare.getSharedAt()
        );
    }
}