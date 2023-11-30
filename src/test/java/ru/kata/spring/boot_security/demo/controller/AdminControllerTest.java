package ru.kata.spring.boot_security.demo.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.RoleServices;
import ru.kata.spring.boot_security.demo.services.UserServices;

import java.util.Arrays;
import java.util.Collections;

import static org.mockito.Mockito.when;

@WebMvcTest(AdminController.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserServices userServices;

    @MockBean
    private RoleServices roleServices;

    private User user1;
    private User user2;

    @BeforeEach
    public void setup() {
        user1 = new User(1L, "user1", "password1", "user1@mail.com", Collections.emptySet());
        user2 = new User(2L, "user2", "password2", "user2@mail.com", Collections.emptySet());
    }

    @Test
    public void testIndexWhenUsersExistThenReturnIndexViewWithUsers() throws Exception {
        when(userServices.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/index"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
                .andExpect(MockMvcResultMatchers.view().name("/index"));
    }

    @Test
    public void testIndexWhenNoUsersThenReturnIndexViewWithoutUsers() throws Exception {
        when(userServices.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/index"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"))
                .andExpect(MockMvcResultMatchers.view().name("/index"));
    }

    @Test
    public void testIndexWhenCalledThenReturnsIndexViewAndOkStatus() throws Exception {
        when(userServices.getAllUsers()).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.get("/admin/index"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("/index"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("users"));
    }
}