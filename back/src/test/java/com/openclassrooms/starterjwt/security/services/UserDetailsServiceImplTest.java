package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private MockFactory mockFactory;

    private User user;

    @BeforeEach
    void setUp() {
        user = mockFactory.createUser(false);
    }

    @Test
    void loadUserByUsername_WhenExistingUser_ShouldReturnUserDetails() {

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_WhenNonExistingUser_ShouldThrowException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent@test.com");
        });

        assertEquals("User Not Found with email: nonexistent@test.com", exception.getMessage());
    }

    @Test
    void loadUserByUsername_WhenNullEmail_ShouldThrowException() {
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });

        assertEquals("User Not Found with email: null", exception.getMessage());
    }

    @Test
    void testEquals_ShouldCompareUsersCorrectly() {
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder()
                .id(1L)
                .username("test@test.com")
                .password("password")
                .admin(false)
                .firstName("test")
                .lastName("test")
                .build();

        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(1L)
                .username("different@test.com")
                .password("different")
                .admin(false)
                .firstName("different")
                .lastName("different")
                .build();

        UserDetailsImpl userDetails3 = UserDetailsImpl.builder()
                .id(2L)
                .username("test3@test.com")
                .password("password3")
                .admin(true)
                .firstName("test3")
                .lastName("test3")  
                .build();

        assertTrue(userDetails1.equals(userDetails1)); 
        assertTrue(userDetails1.equals(userDetails2));
        assertFalse(userDetails1.equals(userDetails3)); 
        assertFalse(userDetails1.equals(null)); 
        assertFalse(userDetails1.equals(new Object())); 
    }

}