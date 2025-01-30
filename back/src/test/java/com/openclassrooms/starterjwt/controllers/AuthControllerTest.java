package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.payload.request.LoginRequest;
import com.openclassrooms.starterjwt.payload.request.SignupRequest;
import com.openclassrooms.starterjwt.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.test.admin.email}")
    private String testAdminEmail;

    @Value("${app.test.admin.password}")
    private String testAdminPassword;

    @Value("${app.test.admin.firstname}")
    private String testAdminFirstName;

    @Value("${app.test.admin.lastname}")
    private String testAdminLastName;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private SignupRequest signupRequest;

    @BeforeEach
    void init() {
        signupRequest = new SignupRequest();
        signupRequest.setEmail(testEmail);
        signupRequest.setPassword(testPassword);
        signupRequest.setFirstName(testFirstName);
        signupRequest.setLastName(testLastName);
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
        loginRequest.setEmail(testAdminEmail);
        loginRequest.setPassword(testAdminPassword);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.firstName").value(testAdminFirstName))
                .andExpect(jsonPath("$.lastName").value(testAdminLastName))
                .andExpect(jsonPath("$.username").exists())
                .andExpect(jsonPath("$.admin").value(true));
    }

    @Test
    void loginUser_WhenWrongPassword_ShouldReturn401() throws Exception {

        User user = new User(testEmail, testLastName, testFirstName,
                passwordEncoder.encode(testPassword), false);
        
        userRepository.save(user);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail(testEmail);
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
        loginRequest.setPassword(testPassword);

        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized());
    }

}