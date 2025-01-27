package com.openclassrooms.starterjwt.security.jwt;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.openclassrooms.starterjwt.security.services.UserDetailsImpl;
import com.openclassrooms.starterjwt.security.services.UserDetailsServiceImpl;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
@TestPropertySource(locations = "classpath:application-test.properties")
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

    private static final String TEST_TOKEN = "Bearer validToken";

    @Value("${app.test.email}")
    private String email;

    @Value("${app.test.password}")
    private String password;

    @Value("${app.test.firstName}")
    private String firstName;

    @Value("${app.test.lastName}")
    private String lastName;

    @Test
    void doFilterInternal_WithValidToken_ShouldAuthenticate() throws ServletException, IOException {

        when(request.getHeader("Authorization")).thenReturn(TEST_TOKEN);
        when(jwtUtils.validateJwtToken("validToken")).thenReturn(true);
        when(jwtUtils.getUserNameFromJwtToken("validToken")).thenReturn(email);
        
        UserDetails userDetails = UserDetailsImpl.builder()
                .username(email)
                .password(password)
                .firstName(firstName)
                .lastName(lastName)
                .admin(false)
                .build();

        when(userDetailsService.loadUserByUsername(email))
                .thenReturn(userDetails);

        authTokenFilter.doFilterInternal(request, response, filterChain);


        verify(filterChain).doFilter(request, response);
        verify(jwtUtils).validateJwtToken("validToken");
        verify(userDetailsService).loadUserByUsername(email);
    }

    @Test
    void doFilterInternal_WithInvalidToken_ShouldContinueChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(TEST_TOKEN);
        when(jwtUtils.validateJwtToken("validToken")).thenReturn(false);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void doFilterInternal_WithNoToken_ShouldContinueChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtUtils, never()).validateJwtToken(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void doFilterInternal_WithInvalidTokenFormat_ShouldContinueChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("InvalidTokenFormat");

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(jwtUtils, never()).validateJwtToken(any());
        verify(userDetailsService, never()).loadUserByUsername(any());
    }

    @Test
    void doFilterInternal_WithException_ShouldContinueChain() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(TEST_TOKEN);
        when(jwtUtils.validateJwtToken("validToken")).thenThrow(new RuntimeException("Test exception"));

        authTokenFilter.doFilterInternal(request, response, filterChain);

        verify(filterChain).doFilter(request, response);
        verify(userDetailsService, never()).loadUserByUsername(any());
    }
}