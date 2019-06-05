package com.project.repository;

import com.project.model.User;

import java.util.Map;

public interface UserRepository {

    User findByEmail(String email);

    Map<String, User> findAll();

    void save(User user);

    void deleteUserById(String id);

    void deleteUser(User user);

}
