package com.todoflow.repository;

import com.todoflow.entity.Todo;
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
class TodoRepositoryTest {

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

    @Test
    void shouldSaveTodoWithOwner() {

        User user = new User(
                "testuser",
                "test@test.com",
                "test-password",
                Role.USER
        );

        User savedUser = userRepository.save(user);

        Todo todo = new Todo(
                "Spring Boot çalış",
                "TodoFlow projesine devam et",
                savedUser
        );
        todo.setTitle("Spring Boot çalış");
        todo.setDescription("TodoFlow projesine devam et");
        todo.setCompleted(false);
        todo.setOwner(savedUser);

        Todo savedTodo = todoRepository.save(todo);

        assertNotNull(savedTodo.getId());
        assertNotNull(savedTodo.getCreatedAt());
        assertNotNull(savedTodo.getUpdatedAt());

        Todo foundTodo = todoRepository.findById(savedTodo.getId())
                .orElseThrow();

        assertEquals("Spring Boot çalış", foundTodo.getTitle());
        assertEquals(savedUser.getId(), foundTodo.getOwner().getId());
    }
}