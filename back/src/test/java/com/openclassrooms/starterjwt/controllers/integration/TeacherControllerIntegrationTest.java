package com.openclassrooms.starterjwt.controllers.integration;

import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;

import org.junit.jupiter.api.BeforeEach;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
public class TeacherControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TeacherService teacherService;

    @Autowired
    private MockFactory mockFactory;

    private Teacher teacher1;

    @BeforeEach
    void setUp(){
        teacher1 = mockFactory.createTeacher();
    }

    @Test
    @WithMockUser
    public void findById_WhenTeacherExist_ShouldSuccessed() throws Exception {
        when(teacherService.findById(1L)).thenReturn(teacher1);

        mockMvc.perform(get("/api/teacher/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value(teacher1.getFirstName()))
                .andExpect(jsonPath("$.lastName").value(teacher1.getLastName()));
    }

    @Test
    @WithMockUser
    public void findById_WhenTeacherNotExist_ShouldFailed404() throws Exception {
        when(teacherService.findById(999L)).thenReturn(null);

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
    public void findAll_WhenTearchersExist_ShouldSuccess() throws Exception {
    
        Teacher teacher2 = mockFactory.createTeacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);
        
        when(teacherService.findAll()).thenReturn(teachers);

        mockMvc.perform(get("/api/teacher")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(teacher1.getId()))
                .andExpect(jsonPath("$[0].firstName").value(teacher1.getFirstName()))
                .andExpect(jsonPath("$[0].lastName").value(teacher1.getLastName()))
                .andExpect(jsonPath("$[1].id").value(teacher2.getId()))
                .andExpect(jsonPath("$[1].firstName").value(teacher2.getFirstName()))
                .andExpect(jsonPath("$[1].lastName").value(teacher2.getLastName()));
    }
} 