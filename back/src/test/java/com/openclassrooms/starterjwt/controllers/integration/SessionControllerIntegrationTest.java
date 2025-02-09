package com.openclassrooms.starterjwt.controllers.integration;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class SessionControllerIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private SessionRepository sessionRepository;

        @Autowired
        private TeacherRepository teacherRepository;

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private SessionService sessionService;

        @Autowired
        private SessionMapper sessionMapper;

        @Autowired
        private ObjectMapper objectMapper;

        private Session session;
        private SessionDto sessionDto;
        private Session session2;
        private Teacher teacher1;

        @BeforeEach
        void setUp() throws Exception {

                teacher1 = Teacher.builder().id(1L).firstName("Margot").lastName("DELAHAYE").build();
                teacherRepository.save(teacher1);

                session = Session.builder()
                                .id(1L)
                                .name("One session")
                                .date(new Date())
                                .description("One session description")
                                .teacher(teacher1)
                                .build();

                session2 = Session.builder()
                                .id(2L)
                                .name("Two session")
                                .date(new Date())
                                .description("Two session description")
                                .teacher(teacher1)
                                .build();

                sessionRepository.save(session);
                sessionRepository.save(session2);
        }

        @Test
        @WithMockUser
        void findById_WhenSessionExists_ShouldReturnSession() throws Exception {
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
                mockMvc.perform(get("/api/session/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void findById_WhenIdIsNotANumber_ShouldThrowNumberFormatException() throws Exception {
                mockMvc.perform(get("/api/session/abc")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void findAll_WhenSessionExist_ShouldReturnAllSessions() throws Exception {
                mockMvc.perform(get("/api/session")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").value(session.getId()))
                                .andExpect(jsonPath("$[1].id").value(session2.getId()));
        }

        @Test
        @WithMockUser
        void create_WhenCreateSession_ShouldReturnCreatedSession() throws Exception {
                sessionDto = sessionMapper.toDto(session);
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
                sessionDto = sessionMapper.toDto(sessionEmpty);

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

                sessionRepository.save(session);

                sessionDto = sessionMapper.toDto(session);

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
                sessionDto = sessionMapper.toDto(sessionEmpty);

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
                mockMvc.perform(put("/api/session/abc")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(sessionDto)))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void participate_WhenAdded_ShouldSucceed() throws Exception {
                mockMvc.perform(post("/api/session/1/participate/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        void participate_WhenSessionNotFound_ShouldFailed() throws Exception {
                mockMvc.perform(post("/api/session/999/participate/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void participate_WhenParticipantNotFound_ShouldFailed() throws Exception {
                mockMvc.perform(post("/api/session/1/participate/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void participate_WhenIdIsNotAnNumber_ShouldThrowNumberFormatException() throws Exception {
                mockMvc.perform(post("/api/session/abc/participate/cde")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void participate_WhenNotFound_ShouldFailed() throws Exception {
                mockMvc.perform(delete("/api/session/999/participate/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void noLongerParticipate_WhenSuccessful_ShouldSucceed() throws Exception {
                User user = userRepository.findById(2L).orElseThrow(() -> new RuntimeException("User not found"));
                Session sessionParticipate = sessionRepository.findById(2L)
                                .orElseThrow(() -> new RuntimeException("Session not found"));
   
                sessionParticipate.getUsers().add(user);
                sessionRepository.save(sessionParticipate);

                mockMvc.perform(delete("/api/session/2/participate/2")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        void noLongerParticipate_WhenSessionNotFound_ShouldFailed() throws Exception {
                mockMvc.perform(delete("/api/session/999/participate/1")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void noLongerParticipate_WhenParticipantNotFound_ShouldFailed() throws Exception {
                mockMvc.perform(delete("/api/session/1/participate/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser
        void delete_WhenSessionExists_ShouldSucceed() throws Exception {
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
                mockMvc.perform(delete("/api/session/999")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isNotFound());
        }

        @Test
        @WithMockUser
        void delete_WhenIdIsNotAnNumber_ShouldThrowNumberFormatException() throws Exception {
                mockMvc.perform(delete("/api/session/abc")
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isBadRequest());
        }
}