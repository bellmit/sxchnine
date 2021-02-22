package com.project.service;

import com.project.exception.ConfirmPasswordException;
import com.project.exception.IncorrectPasswordException;
import com.project.model.User;
import com.project.repository.UserRepository;
import com.project.util.UserCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import static com.project.util.UserCode.ADMIN;

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
        log.info("get user {}", email);
        return userRepository.findByEmail(email)
                .doOnError(error -> log.error("error occurred during get by email: {}", email, error));
    }

    public Mono<Void> save(User user) {
        log.info("save user {}", user.getEmail());
        return getUserByEmail(user.getEmail().toLowerCase())
                .switchIfEmpty(Mono.just(user))
                .flatMap(u -> Mono.fromCallable(() -> {
                    if (StringUtils.hasText(user.getPassword()) && user.getPassword().length() < 20) {
                        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                        user.setEmail(user.getEmail().toLowerCase());
                    } else {
                        user.setPassword(u.getPassword());
                    }
                    return user;
                }))
                .flatMap(userRepository::save)
                .then()
                .doOnError(error -> log.error("error occurred during saving user", error))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<Void> deleteUserById(String id) {
        log.info("delete user with ID: {}", id);
        return userRepository.deleteUserById(id)
                .doOnError(error -> log.error("error occurred during delete by id {}", id, error));
    }

    public Mono<Void> deleteUserByEmail(String email) {
        log.info("delete user: {}", email);
        return userRepository.deleteUserByEmail(email)
                .doOnError(error -> log.error("error occurred during delete by email: {}", email, error));
    }

    public Mono<User> loginAdmin(String email, String password) {
        log.info("login admin: {}", email);
        return getUserByEmail(email)
                .filter(u -> u.getRole().equalsIgnoreCase(ADMIN.getValue()))
                .flatMap(user -> validateUser(user, password))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<User> login(String email, String password) {
        log.info("login user: {}", email);
        return getUserByEmail(email)
                .flatMap(user -> validateUser(user, password))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<User> validateUser(User user, String password) {
        log.info("validate user: {}", user.getEmail());
        return Mono.fromCallable(() -> {
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return user;
            } else {
                return null;
            }
        });
    }

    public Mono<User> changePassword(String email, String oldPassword, String newPassword, String confirmNewPassword){
        log.info("change password for user: {}", email);
        if (!newPassword.equals(confirmNewPassword)){
            return Mono.error(new ConfirmPasswordException("Confirm password is not correct !"));
        }

        return login(email, oldPassword)
                .switchIfEmpty(Mono.error(new IncorrectPasswordException("Old password is not correct")))
                .flatMap(user -> Mono.fromCallable(() ->{
                    user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                    return user;
                }))
                .flatMap(userRepository::save);
    }

    public Flux<User> getUsers(){
        return userRepository.findAll();
    }
}
