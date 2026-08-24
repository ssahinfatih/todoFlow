package com.todoflow.service;

import com.todoflow.entity.User;

import java.util.Optional;

public interface IUserService {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);
}