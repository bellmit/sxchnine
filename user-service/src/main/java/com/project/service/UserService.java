package com.project.service;

import com.project.model.User;
import com.project.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder bCryptPasswordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public Mono<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .doOnError(error -> log.error("error occurred during get by email: {}",email, error));
    }

    public Mono<Void> save(User user) {
        return Mono.fromCallable(() -> {
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            return user;
        })
                .flatMap(userRepository::save)
                .then()
                .doOnError(error -> log.error("error occurred during saving user", error))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Void> deleteUserById(String id) {
        return userRepository.deleteUserById(id)
                .doOnError(error -> log.error("error occurred during delete by id {}", id, error));
    }

    public Mono<Void> deleteUserByEmail(String email) {
        return userRepository.deleteUserByEmail(email)
                .doOnError(error -> log.error("error occurred during delete by email: {}", email, error));
    }

    public Mono<Boolean> login(String email, String password){
        return getUserByEmail(email)
                .flatMap(user -> validateUser(user, password))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<Boolean> validateUser(User user, String password){
         return Mono.fromCallable(() -> {
            return user != null && bCryptPasswordEncoder.matches(password, user.getPassword());
        });
    }
}
