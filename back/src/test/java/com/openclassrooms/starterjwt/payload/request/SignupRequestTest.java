package com.openclassrooms.starterjwt.payload.request;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.openclassrooms.starterjwt.MockFactory;

@SpringBootTest
class SignupRequestTest {
    private Validator validator;

    @Autowired
    private MockFactory mockFactory;

    private SignupRequest request;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        request = mockFactory.createSignupRequest();
    }

    @Test
    void validate_WhenSignupRequestIsValid_shouldReturnEmptyViolations() {
        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertTrue(violations.isEmpty());
    }

    @Test
    void validate_WhenSignupRequestEmailIsInvalid_ShouldReturnViolation() {
        SignupRequest request = this.mockFactory.createSignupRequest();
        request.setEmail("invalid-email");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("email")));
    }

    @Test
    void validate_WhenSignupRequestFirstNameIsTooShort_ShouldReturnViolation() {
        SignupRequest request = this.mockFactory.createSignupRequest();
        request.setFirstName("Jo");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("firstName")));
    }

    @Test
    void validate_WhenSignupRequestLastNameIsTooShort_ShouldReturnViolation() {
        SignupRequest request = this.mockFactory.createSignupRequest();
        request.setLastName("Do");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("lastName")));
    }

    @Test
    void validate_WhenSignupRequestPasswordIsTooShort_ShouldReturnViolation() {
        SignupRequest request = this.mockFactory.createSignupRequest();
        request.setPassword("1234");

        Set<ConstraintViolation<SignupRequest>> violations = validator.validate(request);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath().toString().equals("password")));
    }

}