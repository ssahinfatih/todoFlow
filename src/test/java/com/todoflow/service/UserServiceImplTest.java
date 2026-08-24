package com.todoflow.service;

import com.todoflow.entity.User;
import com.todoflow.enums.Role;
import com.todoflow.exception.DuplicateUserException;
import com.todoflow.repository.UserRepository;
import com.todoflow.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldFindUserByUsername() {

        User user = new User(
                "fatih",
                "fatih@test.com",
                "password",
                Role.USER
        );

        when(userRepository.findByUsername("fatih"))
                .thenReturn(java.util.Optional.of(user));

        User result = userService.findByUsername("fatih")
                .orElseThrow();

        assertEquals("fatih", result.getUsername());
    }
    @Test
    void shouldSaveUser() {

        User user = new User(
                "fatih",
                "fatih@test.com",
                "password",
                Role.USER
        );

        when(userRepository.findByUsername("fatih"))
                .thenReturn(Optional.empty());

        when(userRepository.findByEmail("fatih@test.com"))
                .thenReturn(Optional.empty());

        when(userRepository.save(user))
                .thenReturn(user);

        User result = userService.save(user);

        assertEquals("fatih", result.getUsername());
    }
    @Test
    void shouldThrowExceptionWhenUsernameExists() {

        User existingUser = new User(
                "fatih",
                "old@test.com",
                "password",
                Role.USER
        );

        when(userRepository.findByUsername("fatih"))
                .thenReturn(Optional.of(existingUser));

        User newUser = new User(
                "fatih",
                "new@test.com",
                "password",
                Role.USER
        );

        assertThrows(
                DuplicateUserException.class,
                () -> userService.save(newUser)
        );
    }
}