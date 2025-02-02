package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;

@SpringBootTest
class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private MockFactory mockFactory;

    Teacher teacher;

    TeacherDto teacherDto;

    @BeforeEach
    void setUp() {
        teacher = mockFactory.createTeacher();
        teacherDto = teacherMapper.toDto(teacher);
    }

    @Test
    void teacherToDto_ShouldMapAllFields() {

        TeacherDto teacherDto = teacherMapper.toDto(teacher);

        assertAll(() -> {
            assertThat(teacherDto).isNotNull();
            assertThat(teacherDto.getId()).isEqualTo(teacher.getId());
            assertThat(teacherDto.getLastName()).isEqualTo(teacher.getLastName());
            assertThat(teacherDto.getFirstName()).isEqualTo(teacher.getFirstName());
        });
    }

    @Test
    void teacherToEntity_ShouldMapAllFields() {
        Teacher teacher = teacherMapper.toEntity(teacherDto);

        assertAll(() -> {
            assertThat(teacher).isNotNull();
            assertThat(teacher.getId()).isEqualTo(teacherDto.getId());
            assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());
            assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
        });
    }

    @Test
    void teachersToEntity_ShouldReturnListOfTeacher() {

        TeacherDto teacherDto2 = mockFactory.createTeacherDto();
        teacherDto2.setId(2L);

        List<TeacherDto> teachersDto = Arrays.asList(teacherDto, teacherDto2);

        List<Teacher> teachers = teacherMapper.toEntity(teachersDto);

        assertThat(teachers).isNotNull();
    }

}