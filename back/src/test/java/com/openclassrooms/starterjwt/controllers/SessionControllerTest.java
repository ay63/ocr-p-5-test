package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Date;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class SessionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SessionService sessionService;

    @Autowired
    private SessionMapper sessionMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Session createMockSession() {

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        Session session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDate(new Date());
        session.setDescription("A relaxing yoga session");
        session.setTeacher(teacher);

        return session;
    }

    @Test
    @WithMockUser
    void findById_ShouldReturnSession_WhenSessionExists() throws Exception {
        Session session = createMockSession();
        when(sessionService.getById(1L)).thenReturn(session);

        mockMvc.perform(get("/api/session/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Yoga Session"));
    }

    @Test
    @WithMockUser
    void findById_ShouldReturn404_WhenSessionDoesNotExist() throws Exception {
        when(sessionService.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/session/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void findAll_ShouldReturnAllSessions() throws Exception {
        Session session1 = createMockSession();
        Session session2 = createMockSession();
        session2.setId(2L);
        when(sessionService.findAll()).thenReturn(Arrays.asList(session1, session2));

        mockMvc.perform(get("/api/session")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    @WithMockUser
    void create_ShouldReturnCreatedSession() throws Exception {
        Session session = createMockSession();
        SessionDto sessionDto = sessionMapper.toDto(session);
        when(sessionService.create(any(Session.class))).thenReturn(session);

        mockMvc.perform(post("/api/session")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Session"));
    }

    @Test
    @WithMockUser
    void update_ShouldReturnUpdatedSession() throws Exception {
        Session session = createMockSession();
        SessionDto sessionDto = sessionMapper.toDto(session);
        when(sessionService.update(eq(1L), any(Session.class))).thenReturn(session);

        mockMvc.perform(put("/api/session/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Yoga Session"));
    }

    @Test
    @WithMockUser
    void delete_ShouldReturn200_WhenSessionExists() throws Exception {
        Session session = createMockSession();
        when(sessionService.getById(1L)).thenReturn(session);
        doNothing().when(sessionService).delete(1L);

        mockMvc.perform(delete("/api/session/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void delete_ShouldReturn404_WhenSessionDoesNotExist() throws Exception {
        when(sessionService.getById(1L)).thenReturn(null);

        mockMvc.perform(delete("/api/session/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    void participate_ShouldReturn200_WhenSuccessful() throws Exception {
        doNothing().when(sessionService).participate(1L, 1L);

        mockMvc.perform(post("/api/session/1/participate/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void noLongerParticipate_ShouldReturn200_WhenSuccessful() throws Exception {
        doNothing().when(sessionService).noLongerParticipate(1L, 1L);

        mockMvc.perform(delete("/api/session/1/participate/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
} 