package com.davidcamelo.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record UserDTO (
    Long id,
    String name,
    String lastName
) { }
