package com.project.controller;

import com.project.model.User;
import com.project.service.UserService;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@WebFluxTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private UserService userService;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testGetUserByEmail() throws Exception {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        when(userService.getUserByEmail(anyString())).thenReturn(Mono.just(user));

        webTestClient.get()
                .uri("/email/toto@gmail.com")
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(User.class)
                .value(responseUser -> assertThat(responseUser).isEqualToComparingFieldByFieldRecursively(user));

        verify(userService).getUserByEmail("toto@gmail.com");
    }

    @Test
    public void testCreateOrSaveUser() throws Exception {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        when(userService.save(any(User.class), any())).thenReturn(Mono.empty());

        webTestClient.post().uri("/save")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(user), User.class)
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        verify(userService).save(any(), any());

    }

    @Test
    public void testDeleteByEmail() throws Exception {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        when(userService.deleteUserByEmail(anyString())).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri("/deleteByEmail/"+user.getEmail())
                .exchange()
                .expectStatus()
                .is2xxSuccessful();

        verify(userService).deleteUserByEmail(user.getEmail().toLowerCase());
    }


    @Test
    public void testLogin() {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        when(userService.login(anyString(), anyString())).thenReturn(Mono.just(user));

        webTestClient.post()
                .uri("/login?email="+user.getEmail()+"&password="+user.getPassword())
                .exchange()
                .expectStatus()
                .is2xxSuccessful()
                .expectBody(User.class)
                .value(result -> assertEquals(user.getId(), result.getId()));

        verify(userService).login(user.getEmail().toLowerCase(), user.getPassword());
    }
}
