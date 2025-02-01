package com.openclassrooms.starterjwt.controllers.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.MockFactory;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void findById_ExistingUser_ShouldReturnUser() throws Exception {

        mockMvc.perform(get("/api/user/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value(MockFactory.EMAIL))
                .andExpect(jsonPath("$.firstName").value(MockFactory.FIRST_NAME))
                .andExpect(jsonPath("$.lastName").value(MockFactory.LAST_NAME));
    }

    @Test
    @WithMockUser
    void findById_NonExistingUser_ShouldReturn404() throws Exception {
        mockMvc.perform(get("/api/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
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
    @WithMockUser
    void deleteUser_NonExistingUser_ShouldReturn404() throws Exception {
        mockMvc.perform(delete("/api/user/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void deleteUser_InvalidId_ShouldReturn400() throws Exception {
        mockMvc.perform(delete("/api/user/invalid"))
                .andExpect(status().isBadRequest());
    }
}