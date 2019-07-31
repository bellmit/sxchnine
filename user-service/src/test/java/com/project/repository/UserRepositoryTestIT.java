package com.project.repository;

import com.project.model.User;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTestIT {

    @Autowired
    private UserRepository userRepository;

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
    public void testFindByEmail(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        userRepository.save(user);

        User savedUser = userRepository.findByEmail(user.getEmail());

        assertThat(savedUser).isEqualToComparingFieldByFieldRecursively(user);
    }

    @Test
    public void testFindAll(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        userRepository.save(user);

        Map<String, User> users = userRepository.findAll();

        assertThat(users).containsValue(user);
    }

    @Test
    public void testSave(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        userRepository.save(user);

        User savedUser = userRepository.findByEmail(user.getEmail());

        assertThat(savedUser).isEqualToComparingFieldByFieldRecursively(user);
    }

    @Test
    public void testDeleteById(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);
        user.setId("TOTO");
        user.setEmail("toto@gmail.com");

        userRepository.save(user);

        userRepository.deleteUser(user);

        User savedUser = userRepository.findByEmail("toto@gmail.com");

        assertThat(savedUser).isNull();
    }


}
