package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    private SessionRepository sessionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);

        session = new Session();
        session.setId(1L);
        session.setUsers(new ArrayList<>());
    }

    @Test
    void create_ShouldReturnCreatedSession() {
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session result = sessionService.create(session);

        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        verify(sessionRepository).save(session);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        sessionService.delete(1L);

        verify(sessionRepository).deleteById(1L);
    }

    @Test
    void findAll_ShouldReturnAllSessions() {
        List<Session> sessions = Arrays.asList(session);
        when(sessionRepository.findAll()).thenReturn(sessions);

        List<Session> result = sessionService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        verify(sessionRepository).findAll();
    }

    @Test
    void getById_WhenSessionExists_ShouldReturnSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        Session result = sessionService.getById(1L);

        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
    }

    @Test
    void getById_WhenSessionDoesNotExist_ShouldReturnNull() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        Session result = sessionService.getById(1L);

        assertNull(result);
    }

    @Test
    void update_ShouldReturnUpdatedSession() {
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        Session result = sessionService.update(1L, session);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(sessionRepository).save(session);
    }

    @Test
    void participate_WhenSessionAndUserExist_ShouldAddUserToSession() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        assertDoesNotThrow(() -> sessionService.participate(1L, 1L));

        verify(sessionRepository).save(session);
        assertTrue(session.getUsers().contains(user));
    }

    @Test
    void participate_WhenSessionNotFound_ShouldThrowNotFoundException() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
    }

    @Test
    void participate_WhenUserAlreadyParticipating_ShouldThrowBadRequestException() {
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
    }


    @Test
    void noLongerParticipate_WhenUserParticipating_ShouldRemoveUser() {
        session.getUsers().add(user);
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(sessionRepository.save(any(Session.class))).thenReturn(session);

        assertDoesNotThrow(() -> sessionService.noLongerParticipate(1L, 1L));

        verify(sessionRepository).save(session);
        assertFalse(session.getUsers().contains(user));
    }

    @Test
    void noLongerParticipate_WhenSessionNotFound_ShouldThrowNotFoundException() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }

    @Test
    void noLongerParticipate_WhenUserNotParticipating_ShouldThrowBadRequestException() {
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
    }
}