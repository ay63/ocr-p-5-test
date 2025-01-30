package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;

@SpringBootTest
class TeacherMapperTest {

    @Autowired
    private TeacherMapper teacherMapper;

    @Test
    void teacherToDto_ShouldMapAllFields() {

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setLastName("Doe");
        teacher.setFirstName("John");


        TeacherDto teacherDto = teacherMapper.toDto(teacher);
        assertThat(teacherDto).isNotNull();
        assertThat(teacherDto.getId()).isEqualTo(teacher.getId());
        assertThat(teacherDto.getLastName()).isEqualTo(teacher.getLastName());
        assertThat(teacherDto.getFirstName()).isEqualTo(teacher.getFirstName());
    }

    @Test
    void teacherToEntity_ShouldMapAllFields() {

       TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Doe");
        teacherDto.setFirstName("John");

        Teacher teacher = teacherMapper.toEntity(teacherDto);
        assertThat(teacher).isNotNull();
        assertThat(teacher.getId()).isEqualTo(teacherDto.getId());
        assertThat(teacher.getLastName()).isEqualTo(teacherDto.getLastName());
        assertThat(teacher.getFirstName()).isEqualTo(teacherDto.getFirstName());
    }


} 