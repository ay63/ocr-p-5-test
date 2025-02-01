package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;

@ActiveProfiles("test")
@SpringBootTest
public class JwtUtilsTest {

    @InjectMocks
    private JwtUtils jwtUtils;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetailsImpl userDetails;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(jwtUtils, "jwtSecret", "openclassrooms-secret-key");
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 86400000);

        lenient().when(authentication.getPrincipal()).thenReturn(userDetails);
        lenient().when(userDetails.getUsername()).thenReturn(MockFactory.EMAIL);    
    }

    @Test
    void generateJwtToken_ShouldGenerateValidToken() {

        String token = jwtUtils.generateJwtToken(authentication);

        assertNotNull(token);
        assertTrue(token.length() > 0);
        assertTrue(jwtUtils.validateJwtToken(token));
    }

    @Test
    void getUserNameFromJwtToken_ShouldReturnCorrectUsername() {
        String username = jwtUtils.getUserNameFromJwtToken(jwtUtils.generateJwtToken(authentication));
        assertEquals(MockFactory.EMAIL, username);
    }

    @Test
    void validateJwtToken_ShouldReturnTrue_WhenTokenIsValid() {
        boolean isValid = jwtUtils.validateJwtToken(jwtUtils.generateJwtToken(authentication));

        assertTrue(isValid);
    }

    @Test
    void validateJwtToken_ShouldReturnFalse_WhenTokenIsInvalid() {
        boolean isValid = jwtUtils.validateJwtToken("invalid.token.here");
        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_ShouldReturnFalse_WhenTokenIsExpired() {
        ReflectionTestUtils.setField(jwtUtils, "jwtExpirationMs", 0);
        String token = jwtUtils.generateJwtToken(authentication);

        assertFalse(jwtUtils.validateJwtToken(token));
    }

    @Test
    void validateJwtToken_ShouldReturnFalse_WhenTokenIsEmpty() {
        boolean isValid = jwtUtils.validateJwtToken("");
        assertFalse(isValid);
    }

    @Test
    void validateJwtToken_ShouldReturnFalse_WhenInvalidSignatureJwt() {
        boolean isValid = jwtUtils.validateJwtToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
        assertFalse(isValid);
    }

}