package com.openclassrooms.starterjwt.controllers.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TeacherRepository teacherRepository;

    private Teacher teacher1;

    private Teacher teacher2;

    @BeforeEach
    void setUp() throws Exception {
        teacher1 = Teacher.builder().id(1L).firstName("Margot").lastName("DELAHAYE").build();
        teacher2 = Teacher.builder().id(2L).firstName("Hélène").lastName("THIERCELIN").build();

        teacherRepository.save(teacher2);
        teacherRepository.save(teacher1);
    }

    @Test
    @WithMockUser
    public void findById_WhenTeacherExist_ShouldSucceed() throws Exception {
        mockMvc.perform(get("/api/teacher/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(teacher1.getId()))
                .andExpect(jsonPath("$.lastName").value(teacher1.getLastName()))
                .andExpect(jsonPath("$.firstName").value(teacher1.getFirstName()));
    }

    @Test
    @WithMockUser
    public void findById_WhenTeacherNotExist_ShouldFailed404() throws Exception {
        mockMvc.perform(get("/api/teacher/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser
    public void findById_WhenIdIsInvalid_ShouldFailed400() throws Exception {
        mockMvc.perform(get("/api/teacher/invalid")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void findAll_WhenTeachersExist_ShouldSucceed() throws Exception {

        mockMvc.perform(get("/api/teacher")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(teacher1.getId()))
                .andExpect(jsonPath("$[0].firstName").value(teacher1.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(teacher1.getLastName()))
                .andExpect(jsonPath("$[0].createdAt").isNotEmpty())
                .andExpect(jsonPath("$[0].updatedAt").isNotEmpty())

                .andExpect(jsonPath("$[1].id").value(teacher2.getId()))
                .andExpect(jsonPath("$[1].firstName").value(teacher2.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(teacher2.getLastName()))
                .andExpect(jsonPath("$[1].createdAt").isNotEmpty())
                .andExpect(jsonPath("$[1].updatedAt").isNotEmpty());
    }
}
