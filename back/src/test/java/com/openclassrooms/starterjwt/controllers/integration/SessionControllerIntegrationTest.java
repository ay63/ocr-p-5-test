package com.openclassrooms.starterjwt.controllers.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private SessionService sessionService;

        @Autowired
        private SessionMapper sessionMapper;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private MockFactory mockFactory;

        private Session session;
        private SessionDto sessionDto;

        @BeforeEach
        void setUp() {
                session = mockFactory.createSession();
                sessionDto = mockFactory.createSessionDto();
        }

        @Test
        @WithMockUser
        void findById_WhenSessionExists_ShouldReturnSession() throws Exception {
                when(sessionService.getById(1L)).thenReturn(session);

                mockMvc.perform(get("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(session.getId()))
                                .andExpect(jsonPath("$.name").value(session.getName()))
                                .andExpect(jsonPath("$.description").value(session.getDescription()))
                                .andExpect(jsonPath("$.date").isNotEmpty())
                                .andExpect(jsonPath("$.teacher_id").value(session.getTeacher().getId()))
                                .andExpect(jsonPath("$.users").isArray());

                assertNotNull(sessionService.getById(session.getId()));
        }

        @Test
        @WithMockUser
        void findById_WhenSessionDoesNotExist_ShouldFailed() throws Exception {
                when(sessionService.getById(1L)).thenReturn(null);

                mockMvc.perform(get("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void findById_WhenIdIsNotANumber_ShouldThrowNumberFormatException() throws Exception {
                when(sessionService.getById(1L)).thenThrow(new NumberFormatException());

                mockMvc.perform(get("/api/session/abc")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void findAll_WhenSessionExist_ShouldReturnAllSessions() throws Exception {
                Session session2 = this.mockFactory.createSession();
                session2.setId(2L);

                when(sessionService.findAll()).thenReturn(Arrays.asList(session, session2));

                mockMvc.perform(get("/api/session")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(session.getId()))
                                .andExpect(jsonPath("$[1].id").value(session2.getId()));
        }

        @Test
        @WithMockUser
        void create_WhenCreateSession_ShouldReturnCreatedSession() throws Exception {
                when(sessionService.getById(session.getId())).thenReturn(session);

                when(sessionService.create(any(Session.class))).thenReturn(session);

                mockMvc.perform(post("/api/session")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(session.getId()))
                                .andExpect(jsonPath("$.name").value(session.getName()))
                                .andExpect(jsonPath("$.description").value(session.getDescription()))
                                .andExpect(jsonPath("$.date").isNotEmpty())
                                .andExpect(jsonPath("$.teacher_id").value(session.getTeacher().getId()))
                                .andExpect(jsonPath("$.users").isArray());
        }

        @Test
        @WithMockUser
        void create_WhenSessionHaveMissingAttrbiutes_ShouldFailed() throws Exception {
                Session sessionEmpty = new Session();
                SessionDto sessionDto = sessionMapper.toDto(sessionEmpty);

                mockMvc.perform(put("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void create_WhenUseIsNotLogin_ShouldFailed() throws Exception {
                mockMvc.perform(put("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void update_WhenSessionUpdated_ShouldReturnUpdatedSession() throws Exception {
                String update = "updated";
                session.setDescription(update);
                session.setName(update);
                when(sessionService.update(1L, session)).thenReturn(session);

                mockMvc.perform(put("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.name").value(update))
                                .andExpect(jsonPath("$.description").value(update));
        }

        @Test
        @WithMockUser
        void update_WhenSessionHaveMissingAttrbiute_ShouldFailed() throws Exception {
                Session sessionEmpty = new Session();
                SessionDto sessionDto = sessionMapper.toDto(sessionEmpty);

                mockMvc.perform(put("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void update_WhenUseIsNotLogin_ShouldFailed() throws Exception {
                mockMvc.perform(put("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void update_WhenIdIsNotANumber_ShouldThrowNumberFormatException() throws Exception {
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
        void delete_WhenSessionExists_ShouldSucceed() throws Exception {
                when(sessionService.getById(1L)).thenReturn(session);
                doNothing().when(sessionService).delete(1L);

                mockMvc.perform(delete("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        void delete_WhenUseIsNotLogin_ShouldFailed() throws Exception {
                mockMvc.perform(put("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isUnauthorized());
        }

        @Test
        @WithMockUser
        void delete_WhenSessionDoesNotExist_ShouldFailed() throws Exception {
                when(sessionService.getById(1L)).thenReturn(null);
                mockMvc.perform(delete("/api/session/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void delete_WhenIdIsNotAnNumber_ShouldThrowNumberFormatException() throws Exception {
                doThrow(new NumberFormatException())
                                .when(sessionService).delete(1L);

                mockMvc.perform(delete("/api/session/abc")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void participate_WhenAdded_ShouldSucceed() throws Exception {
                doNothing().when(sessionService).participate(1L, 1L);
                mockMvc.perform(post("/api/session/1/participate/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        void participate_WhenSessionNotFound_ShouldFailed() throws Exception {
                doThrow(new NotFoundException())
                                .when(sessionService).participate(999L, 1L);

                mockMvc.perform(post("/api/session/999/participate/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void participate_WhenParticipantNotFound_ShouldFailed() throws Exception {
                doThrow(new NotFoundException())
                                .when(sessionService).participate(1L, 999L);

                mockMvc.perform(post("/api/session/1/participate/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void participate_WhenIdIsNotAnNumber_ShouldThrowNumberFormatException() throws Exception {
                doThrow(new NumberFormatException())
                                .when(sessionService).participate(1L, 1L);

                mockMvc.perform(post("/api/session/abc/participate/dfsdfsdfsdf")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void participate_WhenNotFound_ShouldFailed() throws Exception {
                doThrow(new BadRequestException())
                                .when(sessionService).participate(999L, 999L);

                mockMvc.perform(delete("/api/session/999/participate/999L")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void noLongerParticipate_WhenSuccessful_ShouldSucceed() throws Exception {
                doNothing().when(sessionService).noLongerParticipate(1L, 1L);

                mockMvc.perform(delete("/api/session/1/participate/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        void noLongerParticipate_WhenSessionNotFound_ShouldFailed() throws Exception {
                doThrow(new NotFoundException())
                                .when(sessionService).noLongerParticipate(999L, 1L);

                mockMvc.perform(delete("/api/session/999/participate/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void noLongerParticipate_WhenParticipantNotFound_ShouldFailed() throws Exception {
                doThrow(new NotFoundException())
                                .when(sessionService).noLongerParticipate(1L, 999L);

                mockMvc.perform(delete("/api/session/1/participate/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

}