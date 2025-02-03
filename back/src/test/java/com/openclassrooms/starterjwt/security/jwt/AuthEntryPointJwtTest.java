package com.openclassrooms.starterjwt.security.jwt;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.AuthenticationException;

@SpringBootTest
public class AuthEntryPointJwtTest {

    @InjectMocks
    private AuthEntryPointJwt authEntryPointJwt;

    @Mock
    private MockHttpServletRequest request;
    @Mock
    private MockHttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setServletPath("/api/test");
        when(authException.getMessage()).thenReturn("Error message");
    }

    @Test
    void commence_WhenErrorOccured_ShouldSetProperResponseAttributes() throws IOException, ServletException {

        when(authException.getMessage()).thenReturn("Error message");

        authEntryPointJwt.commence(request, response, authException);

        assertEquals(HttpServletResponse.SC_UNAUTHORIZED, response.getStatus());
        assertEquals("application/json", response.getContentType());
        assertTrue(response.getContentAsString().contains("Error message"));
        assertTrue(response.getContentAsString().contains("/api/test"));
        assertTrue(response.getContentAsString().contains("Unauthorized"));
    }
} 