package com.openclassrooms.starterjwt.controllers.unit;

import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest
public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private UserController userController;

    @Autowired
    private MockFactory mockFactory;

    User user;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        user = mockFactory.createUser(false);
    }

    @Test
    void findById_WhenUserExists_ShouldReturnUser() {

        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new com.openclassrooms.starterjwt.dto.UserDto());

        ResponseEntity<?> response = userController.findById("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).findById(1L);
        verify(userMapper).toDto(user);
    }

    @Test
    void findById_WhenUserDoesNotExist_ShouldFailed() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.findById("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findById_WhenIdIsNotNumeric_ShouldFailed() {
        ResponseEntity<?> response = userController.findById("invalid");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void delete_WhenUserExistsAndIsAuthorized_ShouldSucceed() {

        when(userService.findById(1L)).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn(user.getEmail());

        ResponseEntity<?> response = userController.save("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).delete(1L);
    }

    @Test
    void delete_WhenUserIsNotAuthorized_ShouldFailed() {
        when(userService.findById(1L)).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("different@test.com");

        ResponseEntity<?> response = userController.save("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void delete_WhenUserDoesNotExist_ShouldFailed() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.save("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void delete_WhenIdIsNotNumeric_ShouldFailed() {
        ResponseEntity<?> response = userController.save("invalid");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(userService, never()).delete(anyLong());
    }
}