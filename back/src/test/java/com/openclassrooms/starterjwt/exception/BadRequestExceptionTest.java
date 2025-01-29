package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

class BadRequestExceptionTest {

    @Test
    void shouldHaveCorrectAnnotation() {
        ResponseStatus annotation = BadRequestException.class.getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.BAD_REQUEST, annotation.value());
    }

    @Test
    void shouldExtendRuntimeException() {
        BadRequestException exception = new BadRequestException();
        assertTrue(exception instanceof RuntimeException);
    }

    @Test
    void shouldBeAbleToCreateInstance() {
        BadRequestException exception = new BadRequestException();
        assertNotNull(exception);
    }
} 