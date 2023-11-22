package ru.kata.spring.boot_security.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.RoleServices;
import ru.kata.spring.boot_security.demo.services.UserServices;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RestAdminController.class)
public class RestAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServices userServices;

    @MockBean
    private RoleServices roleServices;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setUsername("test");
        user.setPassword("test");
        user.setEmail("test@test.com");
    }

    @Test
    public void testUpdateUserWhenUserIsUpdatedThenReturnUpdatedUser() throws Exception {
        Mockito.doNothing().when(userServices).updateUser(Mockito.any(User.class));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"username\":\"test\",\"password\":\"test\",\"email\":\"test@test.com\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("test"))
                .andExpect(jsonPath("$.password").value("test"))
                .andExpect(jsonPath("$.email").value("test@test.com"));
    }


    @Test
    public void testUpdateUserWhenUserNotFoundThenReturnError() throws Exception {

        Mockito.doThrow(new RuntimeException("User not found"))
                .when(userServices)
                .updateUser(Mockito.any(User.class));

        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"username\":\"test\",\"password\":\"test\",\"email\":\"test@test.com\"}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("User not found"));
    }


    @Test
    public void testUpdateUserWhenUserIsNullThenReturnError() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.patch("/api/admin/update/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());
    }
}