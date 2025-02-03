package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@SpringBootTest
public class AuthTokenFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @Mock
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private AuthTokenFilter authTokenFilter;
    
    @Autowired
    private MockFactory mockFactory;


    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = mockFactory.createUserDetails();
    }

    @Test
    void doFilterInternal_WhenValidToken_ShouldAuthenticateAndCallFilterChain() throws ServletException, IOException {

        when(request.getHeader("Authorization")).thenReturn(MockFactory.TEST_TOKEN);
        when(jwtUtils.validateJwtToken("validToken")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("validToken")).thenReturn(userDetails.getUsername());
        
        when(userDetailsService.loadUserByUsername(userDetails.getUsername()))
                .thenReturn(userDetails);

        authTokenFilter.doFilterInternal(request, response, filterChain);


        verify(filterChain).doFilter(request, response);
        verify(jwtUtils).validateJwtToken("validToken");
        verify(userDetailsService).loadUserByUsername(userDetails.getUsername());
    }

    @Test
    void doFilterInternal_WhenInvalidToken_ShouldCallFilterChainWithoutAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(MockFactory.TEST_TOKEN);
        when(jwtUtils.validateJwtToken("validToken")).thenReturn(false);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void doFilterInternal_WhenNoToken_ShouldCallFilterChainWithoutValidation() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtUtils, never()).validateJwtToken(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void doFilterInternal_WhenInvalidTokenFormat_ShouldCallFilterChainWithoutValidation() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidTokenFormat");

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtUtils, never()).validateJwtToken(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void doFilterInternal_WhenException_ShouldCallFilterChainWithoutAuthentication() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(MockFactory.TEST_TOKEN);
        when(jwtUtils.validateJwtToken("validToken")).thenThrow(new RuntimeException("Test exception"));

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(any());
    }
}