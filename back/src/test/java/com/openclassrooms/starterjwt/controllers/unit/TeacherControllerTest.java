package com.openclassrooms.starterjwt.controllers.unit;

import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    @Autowired
    private MockFactory mockFactory;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacher = mockFactory.createTeacher();
        teacherDto = mockFactory.createTeacherDto();
    }

    @Test
    void findById_WhenIdValid_ShouldReturnTeacher() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        ResponseEntity<?> response = teacherController.findById(teacher.getId().toString());

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDto, response.getBody());
        verify(teacherService).findById(1L);
        verify(teacherMapper).toDto(teacher);
    }

    @Test
    void findById_WhenIdInvalid_ShouldFailed() {
        ResponseEntity<?> response = teacherController.findById("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(teacherMapper);
    }

    @Test
    void findById_WhenIdNotExist_ShoudlFailed() {
        when(teacherService.findById(999L)).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(teacherService).findById(999L);
        verifyNoInteractions(teacherMapper);
    }

    @Test
    void findAll_WhenTeachersExist_ShouldReturnTeachers() {
        List<Teacher> teachers = Arrays.asList(teacher);
        List<TeacherDto> teacherDtos = Arrays.asList(teacherDto);

        when(teacherService.findAll()).thenReturn(teachers);
        when(teacherMapper.toDto(teachers)).thenReturn(teacherDtos);

        ResponseEntity<?> response = teacherController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDtos, response.getBody());
        verify(teacherService).findAll();
        verify(teacherMapper).toDto(teachers);
    }
}