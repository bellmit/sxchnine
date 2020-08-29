package com.project.service;

import com.project.model.User;
import com.project.repository.UserRepository;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);
    @Test
    public void testGetUserByEmail(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        when(userRepository.findByEmail(anyString())).thenReturn(Mono.just(user));

        Mono<User> userByEmail = userService.getUserByEmail("toto@gmail.com");

        assertThat(userByEmail.block()).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void testSave(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        when(userRepository.save(any(User.class))).thenReturn(Mono.empty());
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("$$$");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        userService.save(user).block();

        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue().getPassword()).isEqualTo("$$$");
    }

    @Test
    public void testDeleteUser(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        when(userRepository.deleteUserByEmail(anyString())).thenReturn(Mono.empty());

        userService.deleteUserByEmail(user.getEmail());

        verify(userRepository).deleteUserByEmail(user.getEmail());
    }

    @Test
    public void testLoginOK(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        when(userRepository.findByEmail(anyString())).thenReturn(Mono.just(user));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertTrue(userService.login("toto@gmail.com", "toto").block());

        verify(userRepository).findByEmail("toto@gmail.com");
        verify(bCryptPasswordEncoder).matches(anyString(), anyString());
    }

    @Test
    public void testLoginFail(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        when(userRepository.findByEmail(anyString())).thenReturn(Mono.just(user));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

        assertFalse(userService.login("toto@gmail.com", "toto").block());

        verify(userRepository).findByEmail("toto@gmail.com");
        verify(bCryptPasswordEncoder).matches(anyString(), anyString());
    }

}
