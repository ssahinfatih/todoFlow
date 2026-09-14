package com.todoflow.repository;

import com.todoflow.entity.Todo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<Todo, Long> {

    List<Todo> findAllByOwnerUsername(String username);

    Optional<Todo> findByIdAndOwnerUsername(
            Long id,
            String username
    );
    List<Todo> findAllByOwnerUsernameAndCompleted(
            String username,
            boolean completed
    );
}