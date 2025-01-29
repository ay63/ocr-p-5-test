package com.openclassrooms.starterjwt.repository;

import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class SessionRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private SessionRepository sessionRepository;

    private Session session;
    private Session session2;
    private Teacher teacher;

    private User user;

    @BeforeEach
    void setUp() {

        //@todo improve this code
        teacher = new Teacher();            
        teacher.setFirstName("John");
        teacher.setLastName("Doe");

        entityManager.persist(teacher);

        session = new Session();
        session.setName("Session de yoga");
        session.setDescription("Description de la session");
        session.setDate(new Date());
        session.setTeacher(teacher);
        session.setUsers(new ArrayList<User>());

        session2 = new Session();
        session2.setName("Session de yoga 2");
        session2.setDescription("Description de la session 2");
        session2.setDate(new Date());
        session2.setTeacher(teacher);
        session2.setUsers(new ArrayList<User>());

        user = new User();
        user.setFirstName("Ad");
        user.setLastName("AZ");
        user.setEmail("ad.az@example.com");
        user.setPassword("password");


    }

    @Test
    void findById_ExistingSession_ReturnsSession() {
        entityManager.persist(session);
        entityManager.flush();

        Optional<Session> found = sessionRepository.findById(session.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Session de yoga");
        assertThat(found.get().getDescription()).isEqualTo("Description de la session");
    }

    @Test
    void save_NewSession_ReturnsSavedSession() {
        Session saved = sessionRepository.save(session);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getName()).isEqualTo("Session de yoga");
        assertThat(saved.getDescription()).isEqualTo("Description de la session");
    }

    @Test
    void findAll_ReturnsAllSessions() {
        entityManager.persist(session);
        entityManager.persist(session2);
        entityManager.flush();

        List<Session> sessions = sessionRepository.findAll();

        assertThat(sessions).hasSize(2);
        assertThat(sessions).extracting(Session::getName).containsExactlyInAnyOrder("Session de yoga", "Session de yoga 2");
    }

    @Test
    void deleteById_ExistingSession_RemovesSession() {

        sessionRepository.save(session);
        sessionRepository.deleteById(session.getId());
        Optional<Session> deleted = sessionRepository.findById(session.getId());

        assertThat(deleted).isEmpty();
    }
} 