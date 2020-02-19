package com.project.controller;

import com.project.model.User;
import com.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("all")
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("email/{email:.+}")
    public User getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @PostMapping("save")
    public void createOrSaveUser(@RequestBody User user){
        userService.save(user);
    }

    @DeleteMapping("deleteByUser")
    public void deleteByUser(@RequestBody User user){
        userService.deleteUser(user);
    }

    @DeleteMapping("deleteById/{id}")
    public void deleteUserById(@PathVariable String id){
        userService.deleteUserById(id);
    }

    @PostMapping("login")
    public boolean login(@RequestParam String email, @RequestParam String password){
        return userService.login(email, password);
    }
}
