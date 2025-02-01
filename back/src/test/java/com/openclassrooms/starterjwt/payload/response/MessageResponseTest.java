package com.openclassrooms.starterjwt.payload.response;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MessageResponseTest {

    @Test
    void testMessageResponse() {
        String testMessage = "Test message";
        MessageResponse response = new MessageResponse(testMessage);
        assertEquals(testMessage, response.getMessage());

        String newMessage = "New test message";
        response.setMessage(newMessage);
        assertEquals(newMessage, response.getMessage());
    }
} 