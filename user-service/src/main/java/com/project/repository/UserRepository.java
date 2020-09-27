package com.project.repository;

import com.project.model.User;
import reactor.core.publisher.Mono;

public interface UserRepository {

    Mono<User> findByEmail(String email);

    Mono<User> save(User user);

    Mono<Void> deleteUserById(String id);

    Mono<Void> deleteUserByEmail(String email);

}
