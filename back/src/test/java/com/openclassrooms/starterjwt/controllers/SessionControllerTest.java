package com.openclassrooms.starterjwt.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.factory.EntitiesTestFactory;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Import(EntitiesTestFactory.class)
public class SessionControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private SessionService sessionService;

        @Autowired
        private SessionMapper sessionMapper;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private EntitiesTestFactory entitiesTestFactory;

        private Session session;

        private SessionDto sessionDto;

        @BeforeEach
        void setUp() {
                session = entitiesTestFactory.createSession();
                sessionDto = sessionMapper.toDto(session);
        }

        @Test
        @WithMockUser
        void findById_ShouldReturnSession_WhenSessionExists() throws Exception {
                Session session = entitiesTestFactory.createSession();
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
        void findById_ShouldThrowNumberFormatException_WhenIdIsNotANumber() throws Exception {
                when(sessionService.getById(1L)).thenThrow(new NumberFormatException());

                mockMvc.perform(get("/api/session/abc")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void findAll_ShouldReturnAllSessions() throws Exception {
                Session session2 = entitiesTestFactory.createSession();
                session2.setId(2L);
                when(sessionService.findAll()).thenReturn(Arrays.asList(session, session2));

                mockMvc.perform(get("/api/session")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(1))
                                .andExpect(jsonPath("$[1].id").value(2));
        }

        @Test
        @WithMockUser
        void create_ShouldReturnCreatedSession() throws Exception {

                when(sessionService.create(any(Session.class))).thenReturn(session);

                mockMvc.perform(post("/api/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value("Yoga Session"));
        }

        @Test
        @WithMockUser
        void create_ShouldReturn400_WhenSessionHaveMissingAttrbiute() throws Exception {
                Session sessionEmpty = new Session();
                SessionDto sessionDto = sessionMapper.toDto(sessionEmpty);

                mockMvc.perform(put("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void create_ShouldReturn401_WhenUseIsNotLogin() throws Exception {


                mockMvc.perform(put("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void update_ShouldReturnUpdatedSession() throws Exception {
                Session session = entitiesTestFactory.createSession();
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
        void update_ShouldReturn400_WhenSessionHaveMissingAttrbiute() throws Exception {
                Session sessionEmpty = new Session();
                SessionDto sessionDto = sessionMapper.toDto(sessionEmpty);

                mockMvc.perform(put("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void update_ShouldReturn401_WhenUseIsNotLogin() throws Exception {

                mockMvc.perform(put("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void update_ShouldThrowNumberFormatException_WhenIdIsNotANumber() throws Exception {
                doThrow(NumberFormatException.class)
                                .when(sessionService)
                                .update(any(Long.class), any(Session.class));

                mockMvc.perform(put("/api/session/abc")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void delete_ShouldReturn200_WhenSessionExists() throws Exception {
                when(sessionService.getById(1L)).thenReturn(session);
                doNothing().when(sessionService).delete(1L);

                mockMvc.perform(delete("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        void delete_ShouldReturn401_WhenUseIsNotLogin() throws Exception {
                mockMvc.perform(put("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isUnauthorized());
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
        void delete_ShouldThrowNumberFormatException_WhenIdIsNotANumber() throws Exception {
                doThrow(new NumberFormatException())
                                .when(sessionService).delete(1L);

                mockMvc.perform(delete("/api/session/abc")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
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
        void participate_ShouldReturn404_WhenSessionNotFound() throws Exception {
                doThrow(new NotFoundException())
                                .when(sessionService).participate(999L, 1L);

                mockMvc.perform(post("/api/session/999/participate/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void participate_ShouldReturn404_WhenParticipantNotFound() throws Exception {
                doThrow(new NotFoundException())
                                .when(sessionService).participate(1L, 999L);

                mockMvc.perform(post("/api/session/1/participate/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void participate_ShouldThrowNumberFormatException_WhenIdIsNotANumber() throws Exception {
                doThrow(new NumberFormatException())
                                .when(sessionService).participate(1L, 1L);

                mockMvc.perform(post("/api/session/abc/participate/dfsdfsdfsdf")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void participate_ShouldReturn400_WhenNotFound() throws Exception {
                doThrow(new BadRequestException())
                                .when(sessionService).participate(999L, 999L);

                mockMvc.perform(delete("/api/session/999/participate/999L")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void noLongerParticipate_ShouldReturn200_WhenSuccessful() throws Exception {
                doNothing().when(sessionService).noLongerParticipate(1L, 1L);

                mockMvc.perform(delete("/api/session/1/participate/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        void noLongerParticipate_ShouldReturn404_WhenSessionNotFound() throws Exception {
                doThrow(new NotFoundException())
                                .when(sessionService).noLongerParticipate(999L, 1L);

                mockMvc.perform(delete("/api/session/999/participate/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void noLongerParticipate_ShouldReturn404_WhenParticipantNotFound() throws Exception {
                doThrow(new NotFoundException())
                                .when(sessionService).noLongerParticipate(1L, 999L);

                mockMvc.perform(delete("/api/session/1/participate/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

}