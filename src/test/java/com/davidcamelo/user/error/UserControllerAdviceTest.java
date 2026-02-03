package com.davidcamelo.user.error;

import com.davidcamelo.user.dto.ErrorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserControllerAdviceTest {

    private UserControllerAdvice userControllerAdvice;

    @BeforeEach
    void setUp() {
        userControllerAdvice = new UserControllerAdvice();
    }

    @Test
    void testHandleUserException() {
        // Given
        var errorDTO = ErrorDTO.builder()
                .message("User with id 1 not found")
                .timestamp(new Date())
                .build();
        var exception = new UserNotFoundException(errorDTO);

        // When
        var response = userControllerAdvice.handleUserException(exception);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("User with id 1 not found", response.getBody().message());
        assertNotNull(response.getBody().timestamp());
    }

    @Test
    void testHandleUserException_VerifyStatusCode() {
        // Given
        var errorDTO = ErrorDTO.builder()
                .message("Test error message")
                .timestamp(new Date())
                .build();
        var exception = new UserNotFoundException(errorDTO);

        // When
        var response = userControllerAdvice.handleUserException(exception);

        // Then
        assertEquals(404, response.getStatusCode().value());
    }

    @Test
    void testHandleUserException_VerifyErrorMessage() {
        // Given
        var expectedMessage = "User not found for specific operation";
        var errorDTO = ErrorDTO.builder()
                .message(expectedMessage)
                .timestamp(new Date())
                .build();
        var exception = new UserNotFoundException(errorDTO);

        // When
        var response = userControllerAdvice.handleUserException(exception);

        // Then
        assertNotNull(response.getBody());
        assertEquals(expectedMessage, response.getBody().message());
    }
}
