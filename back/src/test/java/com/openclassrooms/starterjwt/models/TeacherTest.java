package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class TeacherTest {

    @Test
    void testBuilder() {
        LocalDateTime now = LocalDateTime.now();
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Doe", teacher.getLastName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    void testSettersAndGetters() {
        Teacher teacher = new Teacher();
        LocalDateTime now = LocalDateTime.now();

        teacher.setId(1L);
        teacher.setFirstName("Jane");
        teacher.setLastName("Smith");
        teacher.setCreatedAt(now);
        teacher.setUpdatedAt(now);

        assertEquals(1L, teacher.getId());
        assertEquals("Jane", teacher.getFirstName());
        assertEquals("Smith", teacher.getLastName());
        assertEquals(now, teacher.getCreatedAt());
        assertEquals(now, teacher.getUpdatedAt());
    }

    @Test
    void testEqualsAndHashCode() {
        Teacher teacher1 = Teacher.builder().id(1L).firstName("John").lastName("Doe").build();
        Teacher teacher2 = Teacher.builder().id(1L).firstName("Jane").lastName("Smith").build();
        Teacher teacher3 = Teacher.builder().id(2L).firstName("John").lastName("Doe").build();

        assertEquals(teacher1, teacher2); 
        assertNotEquals(teacher1, teacher3); 

        assertEquals(teacher1.hashCode(), teacher2.hashCode());
        assertNotEquals(teacher1.hashCode(), teacher3.hashCode());
    }

    @Test
    void testToString() {
        Teacher teacher = Teacher.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .build();

        String toString = teacher.toString();
        
        assertTrue(toString.contains("id=1"));
        assertTrue(toString.contains("firstName=John"));
        assertTrue(toString.contains("lastName=Doe"));
    }

    @Test
    void testChainedSetters() {
        Teacher teacher = new Teacher()
                .setId(1L)
                .setFirstName("John")
                .setLastName("Doe");

        assertEquals(1L, teacher.getId());
        assertEquals("John", teacher.getFirstName());
        assertEquals("Doe", teacher.getLastName());
    }
} 