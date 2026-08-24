package com.todoflow.repository;

import com.todoflow.entity.TodoShare;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TodoShareRepository extends JpaRepository<TodoShare, Long> {
}
