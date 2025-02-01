package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@SpringBootTest
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Autowired
    private MockFactory mockFactory;

    @Test
    void findAll_ShouldReturnListOfTeachers() {
        Teacher teacher1 = mockFactory.createTeacher();
        Teacher teacher2 = mockFactory.createTeacher();
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
        Teacher expectedTeacher = mockFactory.createTeacher();

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(expectedTeacher));

        Teacher actualTeacher = teacherService.findById(1L);

        assertThat(actualTeacher).isNotNull();
        assertThat(actualTeacher.getId()).isEqualTo(1L);
        assertThat(actualTeacher.getFirstName()).isEqualTo(expectedTeacher.getFirstName());
        assertThat(actualTeacher.getLastName()).isEqualTo(expectedTeacher.getLastName());
    }

    @Test
    void findById_ShouldReturnNull_WhenTeacherDoesNotExist() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.empty());

        Teacher actualTeacher = teacherService.findById(1L);

        assertThat(actualTeacher).isNull();
    }
} 