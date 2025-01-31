package com.openclassrooms.starterjwt.controllers.unit;

import com.openclassrooms.starterjwt.controllers.TeacherController;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {

    @Mock
    private TeacherService teacherService;

    @Mock
    private TeacherMapper teacherMapper;

    @InjectMocks
    private TeacherController teacherController;

    private Teacher teacher;
    private TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("John");
        teacherDto.setLastName("Doe");
    }

    @Test
    void findById_ValidId_ReturnsTeacher() {
        when(teacherService.findById(1L)).thenReturn(teacher);
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        ResponseEntity<?> response = teacherController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teacherDto, response.getBody());
        verify(teacherService).findById(1L);
        verify(teacherMapper).toDto(teacher);
    }

    @Test
    void findById_InvalidId_ReturnsBadRequest() {
        ResponseEntity<?> response = teacherController.findById("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verifyNoInteractions(teacherMapper);
    }

    @Test
    void findById_NonExistentId_ReturnsNotFound() {
        when(teacherService.findById(999L)).thenReturn(null);

        ResponseEntity<?> response = teacherController.findById("999");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(teacherService).findById(999L);
        verifyNoInteractions(teacherMapper);
    }

    @Test
    void findAll_ReturnsAllTeachers() {
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