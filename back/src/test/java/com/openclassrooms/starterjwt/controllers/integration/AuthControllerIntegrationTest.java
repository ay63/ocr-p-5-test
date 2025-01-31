package com.openclassrooms.starterjwt.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.context.TestPropertySource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private SignupRequest signupRequest;

    private MockFactory mockFactory;

    @BeforeEach
    void setUp() {
        mockFactory = new MockFactory();
        signupRequest = mockFactory.createSignupRequest();
    }

    @Test
    void registerUser_WhenSignupRequestIsValid_ShouldSuccessed() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"User registered successfully!\"}"));
    }

    @Test
    void registerUser_ShouldFailed_WhenDuplicateEmail() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Error: Email is already taken!\"}"));

                userRepository.deleteAll();
    }

    @Test
    void loginAdminUser_WhenCredentialsAreValid_ShouldSuccessed() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(MockFactory.ADMIN_EMAIL);
        loginRequest.setPassword(MockFactory.ADMIN_PASSWORD);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(MockFactory.ADMIN_FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(MockFactory.ADMIN_LAST_NAME))
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    void loginUser_WhenWrongPassword_ShouldReturn401() throws Exception {

        User user = new User(MockFactory.EMAIL, MockFactory.LAST_NAME, MockFactory.FIRST_NAME,
                passwordEncoder.encode(MockFactory.PASSWORD), false);
        
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(MockFactory.EMAIL);
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginUser_WhenEmailDoesntExist_ShouldReturn401() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@test.com");
        loginRequest.setPassword(MockFactory.PASSWORD);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

}