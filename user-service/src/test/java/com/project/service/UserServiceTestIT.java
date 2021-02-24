package com.project.service;


import com.project.config.TestRedisConfiguration;
import com.project.model.User;
import com.project.repository.UserRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestRedisConfiguration.class)
@ActiveProfiles("test")
@EmbeddedKafka
public class UserServiceTestIT {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testGetUserByEmail() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        User savedUser = userRepository.save(user)
                .then(userService.getUserByEmail(user.getEmail()))
                .log()
                .block();

        assertThat(savedUser).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void testDeleteUser() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        User savedUser = userRepository.save(user)
                .then(userService.deleteUserByEmail(user.getEmail()))
                .then(userService.getUserByEmail(user.getEmail()))
                .block();

        assertThat(savedUser).isNull();
    }

    @Test
    public void testLoginOK() throws InterruptedException {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);
        user.setEmail("toto@gmail.com");
        user.setPassword("TOTO");

        User userAuth = userRepository.save(user)
                .then(userService.login("toto@gmail.com", "TOTO"))
                .block();

        assertEquals(user.getId(), userAuth.getId());
    }


    @Test
    public void testLoginFail() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        User userAuth = userRepository.save(user)
                .then(userService.login(user.getEmail(), "TOTO"))
                .block();

        assertThat(userAuth.getId()).isNull();
    }
}
