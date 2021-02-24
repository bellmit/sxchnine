package com.project.service;

import com.project.exception.ConfirmPasswordException;
import com.project.exception.IncorrectPasswordException;
import com.project.model.User;
import com.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Random;

import static com.project.util.UserCode.ADMIN;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final UserProducer userProducer;

    public Mono<Void> save(User user, boolean isNew) {
        if (isNew) {
            log.info("save new user: {}", user.getEmail());
            return Mono.fromCallable(() -> encryptPassword(user))
                    .flatMap(userRepository::save)
                    .flatMap(userProducer::pushUserToKafka)
                    .doOnError(error -> log.error("error occurred during saving user", error))
                    .then();
        } else {
            log.info("update user: {}", user.getEmail());
            return getUserByEmail(user.getEmail().toLowerCase())
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
                    .doOnError(error -> log.error("error occurred during saving user", error))
                    .then();
        }
    }

    private User encryptPassword(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
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
                .flatMap(user -> validateUser(user, password));
    }

    public Mono<User> login(String email, String password) {
        log.info("login user: {}", email);
        return getUserByEmail(email)
                .flatMap(user -> validateUser(user, password));
    }

    public Mono<User> forgotPassword(String email){
        log.info("forgot password - user: {}", email);
        return getUserByEmail(email)
                .map(user -> {
                    String generateRandomPassword = generateRandomPassword();
                    user.setPassword(generateRandomPassword);
                    user.setPlainPassword(generateRandomPassword);
                    user.setForgotPassword(true);
                    return encryptPassword(user);
                })
                .flatMap(userRepository::save)
                .flatMap(userProducer::pushUserToKafka);
    }

    public Mono<User> getUserByEmail(String email) {
        log.info("get user {}", email);
        return userRepository.findByEmail(email)
                .doOnError(error -> log.error("error occurred during get by email: {}", email, error));
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

    private String generateRandomPassword(){
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString()
                .toUpperCase();
    }

    public Mono<User> changePassword(String email, String oldPassword, String newPassword, String confirmNewPassword) {
        log.info("change password for user: {}", email);
        if (!newPassword.equals(confirmNewPassword)) {
            return Mono.error(new ConfirmPasswordException("Confirm password is not correct !"));
        }

        return login(email, oldPassword)
                .switchIfEmpty(Mono.error(new IncorrectPasswordException("Old password is not correct")))
                .flatMap(user -> Mono.fromCallable(() -> {
                    user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                    return user;
                }))
                .flatMap(userRepository::save);
    }

    public Flux<User> getUsers() {
        return userRepository.findAll();
    }
}
