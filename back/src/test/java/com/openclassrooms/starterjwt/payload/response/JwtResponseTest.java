package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class JwtResponseTest {


    @Test
    void testJwtResponseConstructorAndGetters() {
        JwtResponse jwtResponse = new JwtResponse("test.jwt.token", 1L, "testUser", "John", "Doe", true);

        assertAll(() -> {
            assertEquals("test.jwt.token", jwtResponse.getToken());
            assertEquals("Bearer", jwtResponse.getType());
            assertEquals(1L, jwtResponse.getId());
            assertEquals("testUser", jwtResponse.getUsername());
            assertEquals("John", jwtResponse.getFirstName());
            assertEquals("Doe", jwtResponse.getLastName());
            assertTrue(jwtResponse.getAdmin());
        });
    }
} 