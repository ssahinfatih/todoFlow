package com.todoflow.repository;

import com.todoflow.entity.Todo;
import com.todoflow.entity.TodoShare;
import com.todoflow.entity.User;
import com.todoflow.enums.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@SpringBootTest
class TodoShareRepositoryTest {

    @Container
    static PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:17")
                    .withDatabaseName("todoflow_test")
                    .withUsername("test")
                    .withPassword("test");

    @DynamicPropertySource
    static void configureDatabase(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private TodoShareRepository todoShareRepository;

    @Test
    void shouldShareTodoWithUser() {

        User owner = new User(
                "owner",
                "owner@test.com",
                "password",
                Role.USER
        );

        User sharedUser = new User(
                "shared",
                "shared@test.com",
                "password",
                Role.USER
        );

        User savedOwner = userRepository.save(owner);
        User savedSharedUser = userRepository.save(sharedUser);

        Todo todo = new Todo(
                "Ortak Todo",
                "Arkadaşlarla takip edilecek",
                savedOwner
        );

        Todo savedTodo = todoRepository.save(todo);

        TodoShare share = new TodoShare(
                savedTodo,
                savedSharedUser
        );
        share.setTodo(savedTodo);
        share.setUser(savedSharedUser);

        TodoShare savedShare = todoShareRepository.save(share);

        assertNotNull(savedShare.getId());
        assertNotNull(savedShare.getSharedAt());

        assertEquals(savedTodo.getId(), savedShare.getTodo().getId());
        assertEquals(savedSharedUser.getId(), savedShare.getUser().getId());
    }
}