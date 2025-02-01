package com.openclassrooms.starterjwt.controllers.unit;

import com.openclassrooms.starterjwt.MockFactory;
import com.openclassrooms.starterjwt.controllers.SessionController;
import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.services.SessionService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@SpringBootTest
class SessionControllerTest {

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private SessionService sessionService;

    @Mock
    private SessionMapper sessionMapper;

    @Autowired
    private MockFactory mockFactory;

    private Session session;
    private SessionDto sessionDto;

    @BeforeEach
    void setUp() {
        session = mockFactory.createSession();
        sessionDto = mockFactory.createSessionDto();
    }

    @Test
    void findById_Success() {
        when(sessionService.getById(1L)).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void findById_NotFound() {
        when(sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.findById("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void findById_BadRequest() {
        ResponseEntity<?> response = sessionController.findById("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void findAll_Success() {
        List<Session> sessions = Arrays.asList(session);
        List<SessionDto> sessionDtos = Arrays.asList(sessionDto);
        
        when(sessionService.findAll()).thenReturn(sessions);
        when(sessionMapper.toDto(sessions)).thenReturn(sessionDtos);

        ResponseEntity<?> response = sessionController.findAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDtos, response.getBody());
    }

    @Test
    void create_Success() {
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.create(any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.create(sessionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void update_Success() {
        when(sessionMapper.toEntity(any(SessionDto.class))).thenReturn(session);
        when(sessionService.update(anyLong(), any(Session.class))).thenReturn(session);
        when(sessionMapper.toDto(session)).thenReturn(sessionDto);

        ResponseEntity<?> response = sessionController.update("1", sessionDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(sessionDto, response.getBody());
    }

    @Test
    void update_BadRequest() {
        ResponseEntity<?> response = sessionController.update("invalid", sessionDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void delete_Success() {
        when(sessionService.getById(1L)).thenReturn(session);
        doNothing().when(sessionService).delete(1L);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).delete(1L);
    }

    @Test
    void delete_NotFound() {
        when(sessionService.getById(1L)).thenReturn(null);

        ResponseEntity<?> response = sessionController.save("1");

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    void delete_BadRequest() {
        ResponseEntity<?> response = sessionController.save("invalid");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).delete(anyLong());
    }

    @Test
    void participate_Success() {
        doNothing().when(sessionService).participate(1L, 1L);

        ResponseEntity<?> response = sessionController.participate("1", "1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).participate(1L, 1L);
    }

    @Test
    void participate_BadRequest() {
        ResponseEntity<?> response = sessionController.participate("invalid", "1");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).participate(anyLong(), anyLong());
    }

    @Test
    void noLongerParticipate_Success() {
        doNothing().when(sessionService).noLongerParticipate(1L, 1L);

        ResponseEntity<?> response = sessionController.noLongerParticipate("1", "1");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(sessionService, times(1)).noLongerParticipate(1L, 1L);
    }

    @Test
    void noLongerParticipate_BadRequest() {
        ResponseEntity<?> response = sessionController.noLongerParticipate("invalid", "1");

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(sessionService, never()).noLongerParticipate(anyLong(), anyLong());
    }
} 