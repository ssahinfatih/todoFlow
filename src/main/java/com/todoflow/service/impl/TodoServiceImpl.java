package com.todoflow.service.impl;

import com.todoflow.dto.todo.TodoCreateRequestDto;
import com.todoflow.dto.todo.TodoResponseDto;
import com.todoflow.dto.todo.TodoUpdateRequestDto;
import com.todoflow.entity.Todo;
import com.todoflow.entity.User;
import com.todoflow.exception.NotFoundException;
import com.todoflow.mapper.TodoMapper;
import com.todoflow.repository.TodoRepository;
import com.todoflow.repository.TodoShareRepository;
import com.todoflow.service.ITodoService;
import com.todoflow.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

import java.util.List;

@Service
@AllArgsConstructor
public class TodoServiceImpl implements ITodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;
    private final IUserService userService;
    private final TodoShareRepository todoShareRepository;


    @Override
    public TodoResponseDto createTodo(TodoCreateRequestDto request) {

        String username = getUsername();

        User user = userService
                .findByUsername(username)
                .orElseThrow(() ->
                        new NotFoundException("Kullanıcı bulunamadı")
                );

        Todo todo = todoMapper.toEntity(request);

        todo.setOwner(user);
        todo.setCompleted(false);

        Todo savedTodo = todoRepository.save(todo);

        return todoMapper.toResponseDto(savedTodo);
    }

    @Override
    public List<TodoResponseDto> getAllTodos() {

        String username = getUsername();

        List<TodoResponseDto> ownTodos =
                todoRepository
                        .findAllByOwnerUsername(username)
                        .stream()
                        .map(todoMapper::toResponseDto)
                        .toList();

        List<TodoResponseDto> sharedTodos =
                todoShareRepository
                        .findAllByUserUsername(username)
                        .stream()
                        .map(todoShare -> todoShare.getTodo())
                        .map(todoMapper::toResponseDto)
                        .toList();

        return Stream
                .concat(
                        ownTodos.stream(),
                        sharedTodos.stream()
                )
                .toList();
    }

    @Override
    public TodoResponseDto getTodoById(Long id) {

        String username = getUsername();

        Todo todo = todoRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Todo bulunamadı")
                );

        boolean isOwner =
                todo.getOwner()
                        .getUsername()
                        .equals(username);

        boolean isShared =
                todoShareRepository
                        .existsByTodoIdAndUserUsername(
                                id,
                                username
                        );

        if (!isOwner && !isShared) {
            throw new NotFoundException(
                    "Todo bulunamadı"
            );
        }

        return todoMapper.toResponseDto(todo);
    }

    @Override
    public TodoResponseDto updateTodo(
            Long id,
            TodoUpdateRequestDto request
    ) {

        Todo todo = getTodo(id);

        todo.setTitle(request.title());
        todo.setDescription(request.description());

        Todo updatedTodo = todoRepository.save(todo);

        return todoMapper.toResponseDto(updatedTodo);
    }

    @Override
    public void deleteTodo(Long id) {

        Todo todo = getTodo(id);

        todoRepository.delete(todo);
    }

    private Todo getTodo(Long id) {

        String username = getUsername();

        return todoRepository
                .findByIdAndOwnerUsername(id, username)
                .orElseThrow(() ->
                        new NotFoundException("Todo bulunamadı")
                );
    }

    private String getUsername() {

        return SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
    }

    @Override
    public TodoResponseDto updateCompleted(
            Long id,
            boolean completed
    ) {

        Todo todo = getTodo(id);

        todo.setCompleted(completed);

        Todo updatedTodo =
                todoRepository.save(todo);

        return todoMapper.toResponseDto(updatedTodo);
    }

    @Override
    public List<TodoResponseDto> getTodosByCompleted(
            boolean completed
    ) {

        String username = getUsername();

        List<TodoResponseDto> ownTodos =
                todoRepository
                        .findAllByOwnerUsernameAndCompleted(
                                username,
                                completed
                        )
                        .stream()
                        .map(todoMapper::toResponseDto)
                        .toList();

        List<TodoResponseDto> sharedTodos =
                todoShareRepository
                        .findAllByUserUsername(username)
                        .stream()
                        .map(todoShare -> todoShare.getTodo())
                        .filter(todo ->
                                todo.isCompleted() == completed
                        )
                        .map(todoMapper::toResponseDto)
                        .toList();

        return Stream
                .concat(
                        ownTodos.stream(),
                        sharedTodos.stream()
                )
                .toList();
    }
}