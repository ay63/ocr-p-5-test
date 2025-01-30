package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

class SessionTest {

    @Test
    void testSessionBuilder() {
        Date date = new Date();
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .date(date)
                .description("Description test")
                .teacher(teacher)
                .users(users)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertNotNull(session);
        assertEquals(1L, session.getId());
        assertEquals("Yoga Session", session.getName());
        assertEquals(date, session.getDate());
        assertEquals("Description test", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }

    @Test
    void testSettersAndGetters() {
        Session session = new Session();
        Date date = new Date();
        Teacher teacher = new Teacher();
        List<User> users = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();

        session.setId(1L);
        session.setName("Yoga Session");
        session.setDate(date);
        session.setDescription("Description test");
        session.setTeacher(teacher);
        session.setUsers(users);
        session.setCreatedAt(now);
        session.setUpdatedAt(now);

        assertEquals(1L, session.getId());
        assertEquals("Yoga Session", session.getName());
        assertEquals(date, session.getDate());
        assertEquals("Description test", session.getDescription());
        assertEquals(teacher, session.getTeacher());
        assertEquals(users, session.getUsers());
        assertEquals(now, session.getCreatedAt());
        assertEquals(now, session.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        Session session1 = Session.builder().id(1L).build();
        Session session2 = Session.builder().id(1L).build();
        Session session3 = Session.builder().id(2L).build();

        assertEquals(session1, session2);
        assertNotEquals(session1, session3);
        assertEquals(session1.hashCode(), session2.hashCode());
        assertNotEquals(session1.hashCode(), session3.hashCode());
    }

    @Test
    void testToString() {
        Session session = Session.builder()
                .id(1L)
                .name("Yoga Session")
                .description("Description test")
                .build();

        String toString = session.toString();

        assertNotNull(toString);
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("name=Yoga Session"));
        assertTrue(toString.contains("description=Description test"));
    }
}