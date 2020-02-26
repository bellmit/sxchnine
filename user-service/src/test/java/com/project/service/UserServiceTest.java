package com.project.service;

import com.project.model.User;
import com.project.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserService userService;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);
    @Test
    public void testGetUserByEmail(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        when(userRepository.findByEmail(anyString())).thenReturn(user);

        User userByEmail = userService.getUserByEmail("toto@gmail.com");

        assertThat(userByEmail).isEqualToComparingFieldByFieldRecursively(user);
    }

    @Test
    public void testGetAllUsers(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        Map<String, User> map = new HashMap<>();
        map.put("1", user);

        when(userRepository.findAll()).thenReturn(map);

        List<User> users = userService.getAllUsers();

        assertThat(users.get(0)).isEqualToComparingFieldByFieldRecursively(user);
    }

    @Test
    public void testSave(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        doNothing().when(userRepository).save(any(User.class));
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn("$$$");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        userService.save(user);

        verify(userRepository).save(userCaptor.capture());

        assertThat(userCaptor.getValue().getPassword()).isEqualTo("$$$");
    }

    @Test
    public void testDeleteUser(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        doNothing().when(userRepository).deleteUser(any(User.class));

        userService.deleteUser(user);

        verify(userRepository).deleteUser(user);
    }

    @Test
    public void testLoginOK(){
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        when(userRepository.findByEmail(anyString())).thenReturn(user);
        when(bCryptPasswordEncoder.matches(anyString(), anyString())).thenReturn(true);

        assertThat(userService.login("toto@gmail.com", "toto")).isEqualToComparingFieldByFieldRecursively(user);
    }

    @Test
    public void testLoginFail(){
        assertThat(userService.login("toto@gmail.com", "toto")).isNull();
    }

}
