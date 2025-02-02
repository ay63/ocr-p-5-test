package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

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

    @Autowired
    private MockFactory mockFactory;
    
    private Session session;
    private Session session2;
    private Teacher teacher;
    private User user1;
    private User user2;
    private SessionDto sessionDto;

    @BeforeEach
    void setUp() {
        session = mockFactory.createSession();

        session2 = mockFactory.createSession();
        session2.setId(2L);
        session2.setName("Yoga session2");
        session2.setDescription("session2");

        teacher = mockFactory.createTeacher();
       
        user1 = mockFactory.createUser(false);

        user2 = mockFactory.createUser(false);
        user2.setId(2L);

        session.setUsers(Arrays.asList(user1, user2));
        sessionDto = sessionMapper.toDto(session);
    }

    @Test
    void sessionToEntity_ShouldMapAllFields() {

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(userService.findById(1L)).thenReturn(user1);
        when(userService.findById(2L)).thenReturn(user2);

        Session result = sessionMapper.toEntity(sessionDto);

        assertNotNull(result);

        assertAll(() -> {
            assertEquals(sessionDto.getId(), result.getId());
            assertEquals(sessionDto.getName(), result.getName());
            assertEquals(sessionDto.getDescription(), result.getDescription());
            assertEquals(sessionDto.getDate(), result.getDate());
            assertEquals(teacher, result.getTeacher());
            assertEquals(2, result.getUsers().size());
            assertTrue(result.getUsers().contains(user1));
            assertTrue(result.getUsers().contains(user2));
        });

        verify(teacherService).findById(1L);
        verify(userService).findById(1L);
        verify(userService).findById(2L);
    }
    

    @Test
    void sessionToDto_ShouldMapAllFields() {
        assertAll(() -> {
            assertEquals(session.getId(), sessionDto.getId());
            assertEquals(session.getName(), sessionDto.getName());
            assertEquals(session.getDescription(), sessionDto.getDescription());
            assertEquals(session.getDate(), sessionDto.getDate());
            assertEquals(teacher.getId(), sessionDto.getTeacher_id());
            assertEquals(2, sessionDto.getUsers().size());
            assertTrue(sessionDto.getUsers().contains(1L));
            assertTrue(sessionDto.getUsers().contains(2L));
        });
    }

    @Test
    void sessionsListToEntity_ShouldReturnListOfSession() {
        List<SessionDto> dtos = Arrays.asList(sessionDto, sessionMapper.toDto(session2));
        List<Session> result = sessionMapper.toEntity(dtos);
        assertNotNull(result); 
    }

}