package com.project.repository;

import com.project.model.User;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

@Repository
public class UserRepositoryImpl implements UserRepository {
    private static final String KEY = "USER";

    private RedisTemplate redisTemplate;

    private HashOperations<String, String, User> hashOperations;

    public UserRepositoryImpl(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    private void init(){
          hashOperations = redisTemplate.opsForHash();
    }


    public User findUserByEmail(String email) {
        return hashOperations.get(KEY, email);
    }
}
