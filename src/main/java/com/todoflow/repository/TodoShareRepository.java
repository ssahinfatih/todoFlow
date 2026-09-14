package com.todoflow.repository;

import com.todoflow.entity.TodoShare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoShareRepository
        extends JpaRepository<TodoShare, Long> {

    List<TodoShare> findAllByUserUsername(String username);

    Optional<TodoShare> findByIdAndUserUsername(
            Long id,
            String username
    );

    boolean existsByTodoIdAndUserId(
            Long todoId,
            Long userId
    );
    boolean existsByTodoIdAndUserUsername(
            Long todoId,
            String username
    );
}