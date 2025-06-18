package com.davidcamelo.user.dto;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class FilterDTO {
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "id";
    private Integer pageNumber;
    private Integer pageSize;
}
