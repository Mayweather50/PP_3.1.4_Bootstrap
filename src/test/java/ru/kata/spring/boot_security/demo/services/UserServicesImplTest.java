package ru.kata.spring.boot_security.demo.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServicesImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServicesImpl userServices;

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
    public void testGetUserByIdWhenUserExistsThenReturnUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = userServices.getUserById(1L);

        assertEquals(user, result);
    }

    @Test
    public void testGetUserByIdWhenUserDoesNotExistThenThrowException() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> userServices.getUserById(1L));
    }
}