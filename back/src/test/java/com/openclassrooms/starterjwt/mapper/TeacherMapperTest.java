package com.openclassrooms.starterjwt.mapper;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.List;

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
    void teacherToDto_WhenDtoIsNull_ShouldReturnNull() {
        Teacher teacher = null;
        TeacherDto result = teacherMapper.toDto(teacher);
        assertNull(result); 
    }

    @Test
    void teachersListToDto_WhenDtoIsNull_ShouldReturnNull() {
        List<Teacher> teachers = null;
        List<TeacherDto> result = teacherMapper.toDto(teachers);
        assertNull(result); 
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

    
    @Test
void teacherToEntity_WhenDtoIsNull_ShouldReturnNull() {
        TeacherDto dto = null;
        Teacher result = teacherMapper.toEntity(dto);
        assertNull(result); 
    }

    @Test
    void teachersListToEntity_WhenDtoIsNull_ShouldReturnNull() {
        List<TeacherDto> dtos = null;
        List<Teacher> result = teacherMapper.toEntity(dtos);
        assertNull(result); 
    }


    @Test
    void teachersToEntity_ShouldReturnListOfTeacher() 
    {
        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setLastName("Doe");
        teacherDto.setFirstName("John");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto.setId(2L);
        teacherDto.setLastName("Doe 2");
        teacherDto.setFirstName("John 2");

        List<TeacherDto> teachersDto = Arrays.asList(teacherDto, teacherDto2);

        List<Teacher> teachers = teacherMapper.toEntity(teachersDto);
        
        assertThat(teachers).isNotNull();
    }




} 