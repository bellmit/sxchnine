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
        return userRepository.findByEmail(email)
                .doOnError(error -> log.error("error occurred during get by email: {}", email, error));
    }

    public Mono<Void> save(User user) {
        return getUserByEmail(user.getEmail())
                .switchIfEmpty(Mono.just(user))
                .flatMap(u -> Mono.fromCallable(() -> {
                    if (StringUtils.hasText(user.getPassword())) {
                        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
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
        return userRepository.deleteUserById(id)
                .doOnError(error -> log.error("error occurred during delete by id {}", id, error));
    }

    public Mono<Void> deleteUserByEmail(String email) {
        return userRepository.deleteUserByEmail(email)
                .doOnError(error -> log.error("error occurred during delete by email: {}", email, error));
    }

    public Mono<User> loginAdmin(String email, String password) {
        return getUserByEmail(email)
                .filter(u -> u.getRole().equalsIgnoreCase(ADMIN.getValue()))
                .flatMap(user -> validateUser(user, password))
                .subscribeOn(Schedulers.boundedElastic());
    }

    public Mono<User> login(String email, String password) {
        return getUserByEmail(email)
                .flatMap(user -> validateUser(user, password))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private Mono<User> validateUser(User user, String password) {
        return Mono.fromCallable(() -> {
            if (bCryptPasswordEncoder.matches(password, user.getPassword())) {
                return user;
            } else {
                return null;
            }
        });
    }

    public Mono<User> changePassword(String email, String oldPassword, String newPassword, String confirmNewPassword){
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
}
