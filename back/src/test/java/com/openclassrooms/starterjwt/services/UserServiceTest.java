package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private static final Long USER_ID = 1L;

    @Test
    void deleteUser_ShouldCallRepository() {
        userService.delete(USER_ID);
        verify(userRepository, times(1)).deleteById(USER_ID);
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {
        User expectedUser = new User();
        expectedUser.setId(USER_ID);
        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(expectedUser));

        User result = userService.findById(USER_ID);
        assertNotNull(result);
        assertEquals(USER_ID, result.getId());
        verify(userRepository, times(1)).findById(USER_ID);
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldReturnNull() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());

        User result = userService.findById(USER_ID);

        assertNull(result);
        verify(userRepository, times(1)).findById(USER_ID);
    }
}