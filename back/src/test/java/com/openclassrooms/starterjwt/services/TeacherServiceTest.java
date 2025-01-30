package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        teacherService = new TeacherService(teacherRepository);
    }

    @Test
    void findAll_ShouldReturnListOfTeachers() {
        Teacher teacher1 = new Teacher();
        teacher1.setId(1L);
        teacher1.setFirstName("John");
        teacher1.setLastName("Doe");

        Teacher teacher2 = new Teacher();
        teacher2.setId(2L);
        teacher2.setFirstName("Jane");
        teacher2.setLastName("Smith");

        List<Teacher> expectedTeachers = Arrays.asList(teacher1, teacher2);
        when(teacherRepository.findAll()).thenReturn(expectedTeachers);

        List<Teacher> actualTeachers = teacherService.findAll();

        assertThat(actualTeachers).isNotNull();
        assertThat(actualTeachers).hasSize(2);
        assertThat(actualTeachers).isEqualTo(expectedTeachers);
    }

    @Test
    void findById_ShouldReturnTeacher_WhenTeacherExists() {
        Teacher expectedTeacher = new Teacher();
        expectedTeacher.setId(1L);
        expectedTeacher.setFirstName("John");
        expectedTeacher.setLastName("Doe");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(expectedTeacher));

        Teacher actualTeacher = teacherService.findById(1L);

        assertThat(actualTeacher).isNotNull();
        assertThat(actualTeacher.getId()).isEqualTo(1L);
        assertThat(actualTeacher.getFirstName()).isEqualTo("John");
        assertThat(actualTeacher.getLastName()).isEqualTo("Doe");
    }

    @Test
    void findById_ShouldReturnNull_WhenTeacherDoesNotExist() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        Teacher actualTeacher = teacherService.findById(1L);

        assertThat(actualTeacher).isNull();
    }
} 