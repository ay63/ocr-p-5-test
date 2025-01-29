package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.factory.EntitiesTestFactory;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
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
@Import(EntitiesTestFactory.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${app.test.email}")
    private String testEmail;

    @Value("${app.test.password}")
    private String testPassword;

    @Value("${app.test.firstname}")
    private String testFirstName;

    @Value("${app.test.lastname}")
    private String testLastName;

    @Autowired
    private EntitiesTestFactory entitiesTestFactory;

    @AfterEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    void registerUser_Success() throws Exception {
        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(testEmail);
        signupRequest.setPassword(testPassword);
        signupRequest.setFirstName(testFirstName);
        signupRequest.setLastName(testLastName);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"User registered successfully!\"}"));
    }

    @Test
    void registerUser_DuplicateEmail() throws Exception {
        entitiesTestFactory.createAndSaveUser(false);

        SignupRequest signupRequest = new SignupRequest();
        signupRequest.setEmail(testEmail);
        signupRequest.setPassword(testPassword);
        signupRequest.setFirstName(testFirstName);
        signupRequest.setLastName(testLastName);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().string("{\"message\":\"Error: Email is already taken!\"}"));
    }

    @Test
    void loginUser_Success() throws Exception {
        entitiesTestFactory.createAndSaveUser(false);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(testEmail);
        loginRequest.setPassword(testPassword);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(testFirstName))
                .andExpect(jsonPath("$.lastName").value(testLastName))
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.admin").value(false));
    }

    @Test
    void loginUser_WrongPassword() throws Exception {
        entitiesTestFactory.createAndSaveUser(false);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(testEmail);
        loginRequest.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void loginUser_NonExistentEmail() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("nonexistent@test.com");
        loginRequest.setPassword(testPassword);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }
}