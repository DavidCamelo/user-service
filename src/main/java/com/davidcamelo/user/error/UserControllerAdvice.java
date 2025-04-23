package com.davidcamelo.user.error;

import com.davidcamelo.user.api.UserController;
import com.davidcamelo.user.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice(assignableTypes = { UserController.class })
public class UserControllerAdvice {

    @ExceptionHandler(value = { UserException.class })
    public ResponseEntity<ErrorDTO> handleUserException(UserException ex) {
        return new ResponseEntity<>(ErrorDTO.builder().message(ex.getMessage()).timestamp(new Date()).build(), HttpStatus.NOT_FOUND);
    }
}
