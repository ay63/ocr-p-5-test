package com.openclassrooms.starterjwt.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Value("${app.test.email}")
    private String email;

    @Test
    @WithMockUser(username = "john.doe@studio.com")
    void findById_ExistingUser_ShouldReturnUser() throws Exception {

        mockMvc.perform(get("/api/user/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(email))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @WithMockUser(username = "john.doe@studio.com")
    void findById_NonExistingUser_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "john.doe@studio.com")
    void findById_InvalidId_ShouldReturn400() throws Exception {
        mockMvc.perform(get("/api/user/invalid"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(username = "john.doe@studio.com")
    void deleteUser_OwnAccount_ShouldSucceed() throws Exception {
        mockMvc.perform(delete("/api/user/2"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "other@test.com")
    void deleteUser_OtherUserAccount_ShouldReturn401() throws Exception {
        mockMvc.perform(delete("/api/user/2"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "john.doe@studio.com")
    void deleteUser_NonExistingUser_ShouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "john.doe@studio.com")
    void deleteUser_InvalidId_ShouldReturn400() throws Exception {
        mockMvc.perform(delete("/api/user/invalid"))
                .andExpect(status().isBadRequest());
    }
}