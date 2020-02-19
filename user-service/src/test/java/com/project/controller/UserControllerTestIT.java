package com.project.controller;

import com.project.config.ResourceServerConfig;
import com.project.model.User;
import com.project.repository.UserRepository;
import com.project.service.UserService;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import redis.embedded.RedisServer;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("test")
@Import(ResourceServerConfig.class)
public class UserControllerTestIT {
    
    @Autowired
    private TestRestTemplate testRestTemplate;
    
    @Autowired
    private UserService userService;
    
    public static RedisServer redisServer = new RedisServer();
    
    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .scanClasspathForConcreteTypes(true)
            .ignoreRandomizationErrors(true);
    
    @BeforeClass
    public static void setup(){
        redisServer.start();
    }
    
    @AfterClass
    public static void teardown(){
        redisServer.stop();
    }
    
    @Test
    public void testGetUsers(){
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        userService.save(user);

        ResponseEntity<List<User>> response = testRestTemplate.exchange("/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<User>>() {});

        assertThat(response.getBody()).contains(user);
    }

    @Test
    public void testGetUserByEmail(){
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        userService.save(user);

        User savedUser = testRestTemplate.getForObject("/email/"+user.getEmail(), User.class);

        assertThat(savedUser).isEqualToComparingFieldByFieldRecursively(user);
    }

    @Test
    public void testCreateOrSaveUser(){
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        testRestTemplate.postForObject("/save", user, User.class);

        User savedUser = userService.getUserByEmail(user.getEmail());

        assertThat(savedUser).isEqualToIgnoringGivenFields(user, "password");
    }

    @Test
    public void testDeleteByUser(){
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);
        user.setEmail("toto@gmail.com");

        userService.save(user);

        HttpEntity<User> httpEntity = new ResponseEntity<>(user, HttpStatus.CREATED);

        testRestTemplate.exchange("/deleteByUser", HttpMethod.DELETE,
                httpEntity,
                Object.class,
                user);

        User savedUser = userService.getUserByEmail("toto@gmail.com");

        assertThat(savedUser).isNull();
    }

    @Test
    public void testLogin(){
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);
        user.setEmail("toto@gmail.com");
        user.setPassword("toto");

        userService.save(user);

        User userToRequest = new User();
        userToRequest.setEmail("toto@gmail.com");
        userToRequest.setPassword("toto");

        Boolean response = testRestTemplate.postForObject("/login?email="+user.getEmail()+"&password="+user.getPassword(), userToRequest, Boolean.class);

        assertThat(response).isTrue();
    }
}
