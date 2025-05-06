package com.davidcamelo.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.util.Date;

@Builder
public record ErrorDTO (
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        String message,
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        Date timestamp
) { }
