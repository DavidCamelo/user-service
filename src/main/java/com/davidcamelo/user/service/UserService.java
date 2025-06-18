package com.davidcamelo.user.service;

import com.davidcamelo.user.dto.FilterDTO;
import com.davidcamelo.user.dto.UserDTO;
import org.springframework.data.domain.Page;

public interface UserService {
    UserDTO create(UserDTO userDTO);
    UserDTO getById(Long id);
    Page<UserDTO> getAll(FilterDTO filterDTO);
    UserDTO update(Long id, UserDTO userDTO);
    void delete(Long id);
}
