package com.project.repository;

import com.project.model.User;

public interface UserRepository {

    public User findUserByEmail(String email);
}
