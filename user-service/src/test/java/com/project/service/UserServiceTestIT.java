package com.project.service;

import com.project.model.User;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserServiceTestIT {

    @Autowired
    private UserService userService;

    private static RedisServer redisServer = new RedisServer();

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @BeforeClass
    public static void setup(){
        redisServer.start();
    }

    @AfterClass
    public static void tearDown(){
        redisServer.stop();
    }

    @Test
    public void testGetUserByEmail(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        userService.save(user);

        User savedUser = userService.getUserByEmail(user.getEmail());

        assertThat(savedUser).isEqualToComparingFieldByFieldRecursively(user);
    }

    @Test
    public void testGetAllUsers(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        userService.save(user);

        List<User> allUsers = userService.getAllUsers();

        assertThat(allUsers).contains(user);
    }

    @Test
    public void testDeleteUser(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        userService.save(user);

        userService.deleteUser(user);

        User savedUser = userService.getUserByEmail(user.getEmail());

        assertThat(savedUser).isNull();
    }

    @Test
    public void testLoginOK(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);
        user.setEmail("toto@gmail.com");
        user.setPassword("TOTO");

        userService.save(user);

        assertThat(userService.login("toto@gmail.com", "TOTO")).isEqualToComparingFieldByFieldRecursively(user);
    }


    @Test
    public void testLoginFail(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        assertThat(userService.login(user.getEmail(), user.getPassword())).isNull();
    }
}
