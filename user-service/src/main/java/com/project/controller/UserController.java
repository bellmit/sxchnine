package com.project.controller;

import com.project.model.Subscription;
import com.project.model.User;
import com.project.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public Flux<User> getUsers(){
        return userService.getUsers();
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
        return userService.deleteUserByEmail(email.toLowerCase());
    }

    @DeleteMapping("/deleteById/{id}")
    public Mono<Void> deleteUserById(@PathVariable String id){
        return userService.deleteUserById(id);
    }

    @PostMapping("/login")
    public Mono<User> login(@RequestParam String email, @RequestParam String password){
        return userService.login(email.toLowerCase(), password);
    }

    @PostMapping("/loginAdmin")
    public Mono<User> loginAdmin(@RequestParam String email, @RequestParam String password){
        return userService.loginAdmin(email.toLowerCase(), password);
    }

    @PostMapping("/changePassword")
    public Mono<User> changePassword(@RequestParam String email,
                                     @RequestParam String oldPassword,
                                     @RequestParam String newPassword,
                                     @RequestParam String confirmNewPassword){

        return userService.changePassword(email.toLowerCase(), oldPassword, newPassword, confirmNewPassword);
    }

}
