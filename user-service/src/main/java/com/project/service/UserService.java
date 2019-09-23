package com.project.service;

import com.project.model.User;
import com.project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class UserService {

    private UserRepository userRepository;

    private PasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return new ArrayList(userRepository.findAll().values());
    }

    public void save(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void deleteUserById(String id) {
        userRepository.deleteUserById(id);
    }

    public void deleteUser(User user) {
        userRepository.deleteUser(user);
    }

    public boolean login(String email, String password){
        User user = getUserByEmail(email);
        if (user != null){
            return bCryptPasswordEncoder.matches(password, user.getPassword());
        }
        return false;
    }
}
