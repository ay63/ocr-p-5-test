package com.openclassrooms.starterjwt.controllers.unit;

import com.openclassrooms.starterjwt.controllers.UserController;
import com.openclassrooms.starterjwt.mapper.UserMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
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

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void findById_ShouldReturnUser_WhenUserExists() {
        User user = new User();
        user.setId(1L);
        when(userService.findById(1L)).thenReturn(user);
        when(userMapper.toDto(user)).thenReturn(new com.openclassrooms.starterjwt.dto.UserDto());

        ResponseEntity<?> response = userController.findById("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).findById(1L);
        verify(userMapper).toDto(user);
    }

    @Test
    void findById_ShouldReturnNotFound_WhenUserDoesNotExist() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.findById("1");
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void findById_ShouldReturnBadRequest_WhenIdIsNotNumeric() {
        ResponseEntity<?> response = userController.findById("invalid");
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    void delete_ShouldReturnOk_WhenUserExistsAndIsAuthorized() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        
        when(userService.findById(1L)).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("test@test.com");

        ResponseEntity<?> response = userController.save("1");
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(userService).delete(1L);
    }

    @Test
    void delete_ShouldReturnUnauthorized_WhenUserIsNotAuthorized() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        
        when(userService.findById(1L)).thenReturn(user);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("different@test.com");

        ResponseEntity<?> response = userController.save("1");
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void delete_ShouldReturnNotFound_WhenUserDoesNotExist() {
        when(userService.findById(1L)).thenReturn(null);

        ResponseEntity<?> response = userController.save("1");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        verify(userService, never()).delete(anyLong());
    }

    @Test
    void delete_ShouldReturnBadRequest_WhenIdIsNotNumeric() {
        ResponseEntity<?> response = userController.save("invalid");

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        verify(userService, never()).delete(anyLong());
    }
} 