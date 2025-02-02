package com.openclassrooms.starterjwt.security.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;

import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
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
    void loadUserByUsername_WithExistingUser_ShouldReturnUserDetails() {

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getEmail());

        assertNotNull(userDetails);
        assertEquals(user.getEmail(), userDetails.getUsername());
        assertEquals(user.getPassword(), userDetails.getPassword());
    }

    @Test
    void loadUserByUsername_WithNonExistingUser_ShouldThrowException() {
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent@test.com");
        });

        assertEquals("User Not Found with email: nonexistent@test.com", exception.getMessage());
    }

    @Test
    void loadUserByUsername_WithNullEmail_ShouldThrowException() {
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });

        assertEquals("User Not Found with email: null", exception.getMessage());
    }

    @Test
    void testEquals_ShouldCompareUsersCorrectly() {
        User user1 = new User();
        user1.setId(1L);
        user1.setEmail("test@test.com");
        user1.setPassword("password");
        user1.setAdmin(false);
        user1.setFirstName("firstName");
        user1.setLastName("lastName");
        UserDetailsImpl userDetails1 = UserDetailsImpl.builder()
                .id(user1.getId())
                .username(user1.getEmail())
                .password(user1.getPassword())
                .admin(user1.isAdmin())
                .firstName(user1.getFirstName())
                .lastName(user1.getLastName())
                .build();

        
        User user2 = new User();
        user2.setId(1L);
        user2.setEmail("different@test.com"); 
        user2.setPassword("different");
        UserDetailsImpl userDetails2 = UserDetailsImpl.builder()
                .id(user2.getId())
                .username(user2.getEmail())
                .password(user2.getPassword())
                .build();

     
        User user3 = new User();
        user3.setId(2L);
        user3.setEmail("test@test.com");
        user3.setPassword("password");
        UserDetailsImpl userDetails3 = UserDetailsImpl.builder()
                .id(user3.getId())
                .username(user3.getEmail())
                .password(user3.getPassword())
                .build();


        assertTrue(userDetails1.equals(userDetails1)); 
        assertTrue(userDetails1.equals(userDetails2));
        assertFalse(userDetails1.equals(userDetails3)); 
        assertFalse(userDetails1.equals(null)); 
        assertFalse(userDetails1.equals(new Object())); 
    }

}