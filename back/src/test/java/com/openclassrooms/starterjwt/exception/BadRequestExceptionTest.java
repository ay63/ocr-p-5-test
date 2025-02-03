package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {


    @Test
    void badRequest_shouldHaveCorrectAnnotation() {
        ResponseStatus annotation = BadRequestException.class.getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.BAD_REQUEST, annotation.value());
    }

    @Test
    void badRequest_WhenThrow_shouldThrowBadRequestException() {
        assertThrows(BadRequestException.class, () -> {
            throw new BadRequestException();
        });
    }
} 