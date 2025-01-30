package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.Date;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class SessionMapperTest {

    @InjectMocks
    private SessionMapperImpl sessionMapper;

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @Test
    void toEntity_ShouldMapAllFields() {
        SessionDto dto = new SessionDto();
        dto.setId(1L);
        dto.setName("Yoga Session");
        dto.setDescription("Beginner friendly yoga");
        dto.setDate(new Date());
        dto.setTeacher_id(1L);
        dto.setUsers(Arrays.asList(1L, 2L));

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        Session result = sessionMapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        assertEquals(dto.getDescription(), result.getDescription());
        assertEquals(dto.getDate(), result.getDate());
        assertEquals(teacher, result.getTeacher());
        assertEquals(2, result.getUsers().size());
        assertTrue(result.getUsers().contains(user1));
        assertTrue(result.getUsers().contains(user2));

        verify(teacherService).findById(1L);
        verify(userService).findById(1L);
        verify(userService).findById(2L);
    }

    @Test
    void toDto_ShouldMapAllFields() {
        Session session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDescription("Beginner friendly yoga");
        session.setDate(new Date());

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        session.setTeacher(teacher);

        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        session.setUsers(Arrays.asList(user1, user2));

        SessionDto result = sessionMapper.toDto(session);

        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        assertEquals(session.getName(), result.getName());
        assertEquals(session.getDescription(), result.getDescription());
        assertEquals(session.getDate(), result.getDate());
        assertEquals(teacher.getId(), result.getTeacher_id());
        assertEquals(2, result.getUsers().size());
        assertTrue(result.getUsers().contains(1L));
        assertTrue(result.getUsers().contains(2L));
    }

    @Test
    void toEntity_WithNullValues_ShouldHandleGracefully() {
        SessionDto dto = new SessionDto();
        dto.setId(1L);
        dto.setName("Yoga Session");

        Session result = sessionMapper.toEntity(dto);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
        assertEquals(dto.getName(), result.getName());
        assertNull(result.getDescription());
        assertNull(result.getTeacher());
        assertTrue(result.getUsers().isEmpty());
    }

    @Test
    void toDto_WithNullValues_ShouldHandleGracefully() {
        Session session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");

        SessionDto result = sessionMapper.toDto(session);

        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        assertEquals(session.getName(), result.getName());
        assertNull(result.getDescription());
        assertNull(result.getTeacher_id());
        assertTrue(result.getUsers().isEmpty());
    }
} 