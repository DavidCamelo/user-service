package com.davidcamelo.user.error;

import com.davidcamelo.user.dto.ErrorDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserException extends RuntimeException {

    public UserException(ErrorDTO errorDTO) {
        super(errorDTO.message());
        log.error("Error message: {}, timestamp: {}", errorDTO.message(), errorDTO.timestamp(), this);
    }
}
