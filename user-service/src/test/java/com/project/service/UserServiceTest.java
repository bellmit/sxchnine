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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder bCryptPasswordEncoder;

    @Mock
    private UserProducer userProducer;

    @InjectMocks
    private UserService userService;

    private final EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testGetUserByEmail() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        when(userRepository.findByEmail(anyString())).thenReturn(Mono.just(user));

        Mono<User> userByEmail = userService.getUserByEmail("toto@gmail.com");

        assertThat(userByEmail.block()).usingRecursiveComparison().isEqualTo(user);
    }

    @Test
    public void testSaveWithEncode() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);
        user.setPassword("1234");

        when(userRepository.save(any(User.class))).thenReturn(Mono.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Mono.just(user));
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("$$$");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        userService.save(user, false).block();

        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue().getPassword()).isEqualTo("$$$");
    }

    @Test
    public void testSaveWithoutEncode() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);
        user.setPassword("12345678909876543212");

        when(userRepository.save(any(User.class))).thenReturn(Mono.empty());
        when(userRepository.findByEmail(anyString())).thenReturn(Mono.just(user));

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        userService.save(user, false).block();

        verify(userRepository).save(userCaptor.capture());
        verify(bCryptPasswordEncoder, never()).encode(userCaptor.getValue().getPassword());
    }

    @Test
    public void testSaveNewWithEncode() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);
        user.setPassword("12345678909876543212");

        when(userRepository.save(any(User.class))).thenReturn(Mono.just(user));
        when(userProducer.pushUserToKafka(any())).thenReturn(Mono.empty());

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        userService.save(user, true).block();

        verify(userRepository).save(userCaptor.capture());
        verify(bCryptPasswordEncoder, never()).encode(userCaptor.getValue().getPassword());
        verify(userProducer).pushUserToKafka(any());
    }

    @Test
    public void testDeleteUser() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        when(userRepository.deleteUserByEmail(anyString())).thenReturn(Mono.empty());

        userService.deleteUserByEmail(user.getEmail());

        verify(userRepository).deleteUserByEmail(user.getEmail());
    }

    @Test
    public void testLoginOK() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        when(userRepository.findByEmail(anyString())).thenReturn(Mono.just(user));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);

        User userAuth = userService.login("toto@gmail.com", "toto").block();
        assertThat(userAuth.getId()).isEqualTo(user.getId());

        verify(userRepository).findByEmail("toto@gmail.com");
        verify(bCryptPasswordEncoder).matches(anyString(), anyString());
    }

    @Test
    public void testLoginFail() {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        when(userRepository.findByEmail(anyString())).thenReturn(Mono.just(user));
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(false);

        User userAuth = userService.login("toto@gmail.com", "toto").block();
        assertThat(userAuth).isNull();

        verify(userRepository).findByEmail("toto@gmail.com");
        verify(bCryptPasswordEncoder).matches(anyString(), anyString());
    }

}
