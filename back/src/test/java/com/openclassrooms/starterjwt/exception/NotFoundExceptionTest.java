package com.openclassrooms.starterjwt.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

class NotFoundExceptionTest {

    @Test
    void notFound_shouldHaveCorrectAnnotation() {
        ResponseStatus annotation = NotFoundException.class.getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.NOT_FOUND, annotation.value());
    }

    @Test
    void notFound_WhenThrow_shouldThrowNotFoundException() {
        assertThrows(NotFoundException.class, () -> {
            throw new NotFoundException();
        });
    }
}