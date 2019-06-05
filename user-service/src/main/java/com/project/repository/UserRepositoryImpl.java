package com.project.repository;

import com.project.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Map;

@Repository
@Transactional
public class UserRepositoryImpl implements UserRepository {
    private static final String KEY = "USER";

    @Autowired
    private RedisTemplate redisTemplate;
    private HashOperations<String, String, User> hashOperations;

    @PostConstruct
    private void init(){
        hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public User findByEmail(String email) {
        return hashOperations.get(KEY, email);
    }

    @Override
    public Map<String, User> findAll() {
        return hashOperations.entries(KEY);
    }

    @Override
    public void save(User user) {
        hashOperations.put(KEY, user.getEmail(), user);
    }

    @Override
    public void deleteUserById(String id) {
        hashOperations.delete(KEY, id);
    }

    @Override
    public void deleteUser(User user) {
        hashOperations.delete(KEY, user.getEmail());
    }
}
