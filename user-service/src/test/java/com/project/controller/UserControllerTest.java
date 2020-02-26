package com.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.config.ResourceServerConfig;
import com.project.model.User;
import com.project.service.UserService;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.assertj.core.api.Assertions.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
@Import(ResourceServerConfig.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    private EasyRandomParameters easyRandomParameters = new EasyRandomParameters()
            .collectionSizeRange(0, 2)
            .ignoreRandomizationErrors(true)
            .scanClasspathForConcreteTypes(true);

    @Test
    public void testGetUsers() throws Exception {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        when(userService.getAllUsers()).thenReturn(Collections.singletonList(user));

        MvcResult response = mockMvc.perform(get("/all")).andReturn();

        assertThat(response.getResponse().getContentAsString()).isNotNull();
    }

    @Test
    public void testGetUserByEmail() throws Exception {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        when(userService.getUserByEmail(anyString())).thenReturn(user);

        MvcResult response = mockMvc.perform(get("/email/toto@gmail.com")).andReturn();

        User responseUser = objectMapper.readValue(response.getResponse().getContentAsString(), User.class);

        assertThat(responseUser).isEqualToComparingFieldByFieldRecursively(user);
    }

    @Test
    public void testCreateOrSaveUser() throws Exception {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        doNothing().when(userService).save(any(User.class));

        MvcResult response = mockMvc.perform(post("/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.SC_OK);
    }

    @Test
    public void testDeleteByUser() throws Exception {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        doNothing().when(userService).deleteUser(any(User.class));

        MvcResult response = mockMvc.perform(delete("/deleteByUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.SC_OK);
    }

    @Test
    public void testDeleteUserById() throws Exception {
        doNothing().when(userService).deleteUserById(anyString());

        MvcResult response = mockMvc.perform(delete("/deleteById/1")
                .contentType(MediaType.APPLICATION_JSON)).andReturn();

        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.SC_OK);
    }

    @Test
    public void testLogin() throws Exception {
        EasyRandom easyRandom = new EasyRandom();
        User user = easyRandom.nextObject(User.class);

        when(userService.login(anyString(), anyString())).thenReturn(user);

        MvcResult response = mockMvc.perform(post("/login?email="+user.getEmail()+"&password="+user.getPassword())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user))).andReturn();

        assertThat(response.getResponse().getContentAsString()).contains(user.getEmail());
        assertThat(response.getResponse().getStatus()).isEqualTo(HttpStatus.SC_OK);
    }

    @Test
    public void testNotFound() throws Exception {
        EasyRandom easyRandom = new EasyRandom(easyRandomParameters);
        User user = easyRandom.nextObject(User.class);

        mockMvc.perform(post("/saved").
                contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isNotFound());

    }


    @Test
    public void testBadRequest() throws Exception {
        mockMvc.perform(post("/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(""))
                .andExpect(status().isBadRequest());

    }
}
