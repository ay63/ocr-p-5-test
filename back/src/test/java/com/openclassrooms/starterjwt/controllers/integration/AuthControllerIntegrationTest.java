package com.openclassrooms.starterjwt.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private SignupRequest signupRequest;

    private LoginRequest loginRequest;

    @Autowired
    private MockFactory mockFactory;

    @BeforeEach
    void setUp() {
        signupRequest = mockFactory.createSignupRequest();
        loginRequest = mockFactory.createLoginRequest();
    }

    @Test
    void registerUser_WhenSignupRequestIsValid_ShouldSuccessed() throws Exception {
        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"User registered successfully!\"}"));

        assertNotNull(userRepository.findByEmail("newuser@email.com").get());
    }

    @Test
    void registerUser_WhenDuplicateEmail_ShouldFailed() throws Exception {
        signupRequest.setEmail(MockFactory.EMAIL);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Error: Email is already taken!\"}"));

    }

    @Test
    void loginAdminUser_WhenCredentialsAreValid_ShouldSuccessed() throws Exception {

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
    void loginUser_WhenWrongPassword_ShouldFailed() throws Exception {

        loginRequest.setEmail(MockFactory.EMAIL);
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginUser_WhenWrongEmail_ShouldFailed() throws Exception {
        loginRequest.setEmail("wrong@email.fr");
        loginRequest.setPassword(MockFactory.PASSWORD);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginUser_WhenEmailDoesntExist_ShouldFailed() throws Exception {
        loginRequest.setEmail("nonexistent@test.com");
        loginRequest.setPassword(MockFactory.PASSWORD);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

}