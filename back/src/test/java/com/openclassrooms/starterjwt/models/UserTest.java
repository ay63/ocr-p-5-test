package com.openclassrooms.starterjwt.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class UserTest {

        @Test
        void testBuilder() {
                String email = "test@test.com";
                String firstName = "John";
                String lastName = "Doe";
                String password = "password123";
                boolean admin = false;
                LocalDateTime now = LocalDateTime.now();

                User user = User.builder()
                                .email(email)
                                .firstName(firstName)
                                .lastName(lastName)
                                .password(password)
                                .admin(admin)
                                .createdAt(now)
                                .updatedAt(now)
                                .build();

                assertNotNull(user);
                assertEquals(email, user.getEmail());
                assertEquals(firstName, user.getFirstName());
                assertEquals(lastName, user.getLastName());
                assertEquals(password, user.getPassword());
                assertEquals(admin, user.isAdmin());
                assertEquals(now, user.getCreatedAt());
                assertEquals(now, user.getUpdatedAt());
        }

        @Test
        void testSettersAndGetters() {
                LocalDateTime now = LocalDateTime.now();
                String email = "test@test.com";
                String firstName = "John";
                String lastName = "Doe";
                String password = "password123";
                boolean admin = true;

                User user = new User(1L, email, lastName, firstName, password, admin, now, now);

                assertEquals(email, user.getEmail());
                assertEquals(firstName, user.getFirstName());
                assertEquals(lastName, user.getLastName());
                assertEquals(password, user.getPassword());
                assertEquals(admin, user.isAdmin());
                assertEquals(now, user.getCreatedAt());
                assertEquals(now, user.getUpdatedAt());
        }

        @Test
        void testEqualsAndHashCode() {
                User user1 = User.builder().id(1L).email("test1@test.com").firstName("John").lastName("Doe")
                                .password("pass")
                                .admin(false).build();
                User user2 = User.builder().id(1L).email("test2@test.com").firstName("Jane").lastName("Smith")
                                .password("word")
                                .admin(true).build();
                User user3 = User.builder().id(2L).email("test1@test.com").firstName("John").lastName("Doe")
                                .password("pass")
                                .admin(false).build();

                assertEquals(user1, user2, "Users with same ID should be equal");
                assertNotEquals(user1, user3, "Users with different IDs should not be equal");
                assertEquals(user1.hashCode(), user2.hashCode(), "Hash codes should be equal for users with same ID");
                assertNotEquals(user1.hashCode(), user3.hashCode(),
                                "Hash codes should be different for users with different IDs");
        }

        @Test
        void testToString() {
                User user = User.builder()
                                .id(1L)
                                .email("test@test.com")
                                .firstName("John")
                                .lastName("Doe")
                                .password("password123")
                                .admin(false)
                                .build();

                String toString = user.toString();

                assertNotNull(toString);
                assertTrue(toString.contains("test@test.com"));
                assertTrue(toString.contains("John"));
                assertTrue(toString.contains("Doe"));
        }

}