package com.project.controller;

import com.project.config.TestRedisConfiguration;
import com.project.model.User;
import com.project.service.UserService;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Import(TestRedisConfiguration.class)
@ActiveProfiles("test")
public class UserControllerTestIT {
    
    @Autowired
    private WebTestClient webTestClient;
    
    @Autowired
    private UserService userService;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .scanClasspathForConcreteTypes(true)
            .ignoreRandomizationErrors(true);

    @Test
    public void testGetUserByEmail(){
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        userService.save(user).block();

        webTestClient.get()
                .uri("/email/"+user.getEmail())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(User.class)
                .value(savedUser -> assertThat(savedUser).usingRecursiveComparison().isEqualTo(user));
    }

    @Test
    public void testCreateOrSaveUser(){
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        webTestClient.post()
                .uri("/save")
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        User savedUser = userService.getUserByEmail(user.getEmail()).block();

        assertThat(savedUser).isEqualToIgnoringGivenFields(user, "password");
    }

    @Test
    public void testDeleteByUser(){
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);
        user.setEmail("toto@gmail.com");

        userService.save(user).block();

        webTestClient.delete()
                .uri("/deleteByEmail/toto@gmail.com")
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        User savedUser = userService.getUserByEmail("toto@gmail.com").block();

        assertThat(savedUser).isNull();
    }

    @Test
    public void testLogin(){
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);
        user.setEmail("toto@gmail.com");
        user.setPassword("toto1");

        userService.save(user).block();

        webTestClient.post()
                .uri("/login?email="+user.getEmail()+"&password=toto1")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(User.class)
                .value(response -> assertThat(response.getId()).isEqualTo(user.getId()));
    }
}
