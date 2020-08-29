package com.project.controller;

import com.project.model.User;
import com.project.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/email/{email:.+}")
    public Mono<User> getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    @PostMapping("/save")
    public Mono<Void> createOrSaveUser(@RequestBody User user){
        return userService.save(user);
    }

    @DeleteMapping("/deleteByEmail/{email:.+}")
    public Mono<Void> deleteByUserByEmail(@PathVariable String email){
        return userService.deleteUserByEmail(email);
    }

    @DeleteMapping("/deleteById/{id}")
    public Mono<Void> deleteUserById(@PathVariable String id){
        return userService.deleteUserById(id);
    }

    @PostMapping("/login")
    public Mono<Boolean> login(@RequestParam String email, @RequestParam String password){
        return userService.login(email, password);
    }
}
