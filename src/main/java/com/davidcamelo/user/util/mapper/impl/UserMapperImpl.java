package com.davidcamelo.user.util.mapper.impl;

import com.davidcamelo.user.dto.UserDTO;
import com.davidcamelo.user.entity.User;
import com.davidcamelo.user.util.mapper.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO map(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .lastName(user.getLastName())
                .build();
    }

    @Override
    public void map(UserDTO userDTO, User user) {
        user.setName(userDTO.name());
        user.setLastName(userDTO.lastName());
    }
}
