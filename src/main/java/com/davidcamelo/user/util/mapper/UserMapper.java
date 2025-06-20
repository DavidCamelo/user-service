package com.davidcamelo.user.util.mapper;

import com.davidcamelo.user.dto.UserDTO;
import com.davidcamelo.user.entity.User;
import org.springframework.data.domain.Page;

public interface UserMapper {
    UserDTO map(User user);
    void map(UserDTO userDTO, User user);
    Page<UserDTO> mapPage(Page<User> userPage);
}
