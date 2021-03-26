package com.project.service;


import com.project.config.TestRedisConfiguration;
import com.project.model.User;
import com.project.repository.UserRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestRedisConfiguration.class)
@ActiveProfiles("test")
@EmbeddedKafka
public class UserServiceTestIT {

    private static final String EMAIL_TEST = "toto@gmail.com";

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @BeforeEach
    public void teardown(){
        userRepository.deleteUserByEmail(EMAIL_TEST);
    }
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
    public void testLoginOK() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);
        user.setEmail(EMAIL_TEST);
        user.setPassword("TOTO");

        User userAuth = userService.save(user, true)
                .then(userService.login(EMAIL_TEST, "TOTO"))
                .block();

        assertEquals(user.getId(), userAuth.getId());
    }


    @Test
    public void testUpdateUserAndLoginFail() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);
        user.setEmail(EMAIL_TEST);

        userService.save(user, true).block();

        User userAuth = userService.save(user, false)
                .then(userService.login(user.getEmail(), "TOTO"))
                .block();

        assertThat(userAuth).isNull();
    }

    @Test
    public void testForgotPassword() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);
        user.setEmail(EMAIL_TEST);
        user.setPassword("TOTO");

        User userAuth = userService.save(user, true)
                .then(userService.forgotPassword(EMAIL_TEST))
                .block();

        assertThat(userAuth.getPassword()).isNotEqualTo(user.getPassword());
    }

    @Test
    public void testChangePassword() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);
        user.setEmail(EMAIL_TEST);
        user.setPassword("TOTO");

        User userAuth = userService.save(user, true)
                .then(userService.changePassword(EMAIL_TEST, "TOTO",
                        "TATA",
                        "TATA"))
                .block();

        assertThat(userAuth.getPassword()).isNotEqualTo(user.getPassword());
        assertThat(passwordEncoder.matches("TATA", userAuth.getPassword())).isTrue();
    }
}
