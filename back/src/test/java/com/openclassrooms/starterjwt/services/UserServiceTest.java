package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Autowired
    private MockFactory mockFactory;

    private User user;

    @BeforeEach
    void setUp() {
        user = mockFactory.createUser(false);
    }

    @Test
    void deleteUser_ShouldCallRepository() {
        userService.delete(user.getId());
        
        verify(userRepository, times(1)).deleteById(user.getId());
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User result = userService.findById(user.getId());

        assertNotNull(result);
        assertEquals(user.getId(), result.getId());
        verify(userRepository, times(1)).findById(user.getId());
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldReturnNull() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

        User result = userService.findById(user.getId());

        assertNull(result);
        verify(userRepository, times(1)).findById(user.getId());
    }
}