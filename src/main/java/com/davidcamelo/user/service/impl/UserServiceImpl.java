package com.davidcamelo.user.service.impl;

import com.davidcamelo.user.dto.ErrorDTO;
import com.davidcamelo.user.dto.UserDTO;
import com.davidcamelo.user.entity.User;
import com.davidcamelo.user.error.UserException;
import com.davidcamelo.user.repository.UserRepository;
import com.davidcamelo.user.service.UserService;
import com.davidcamelo.user.util.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserDTO create(UserDTO userDTO) {
        return upsert(userDTO, new User());
    }

    @Override
    public UserDTO getById(Long id) {
        return userMapper.map(findById(id));
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll().stream().map(userMapper::map).toList();
    }

    @Override
    public UserDTO update(Long id, UserDTO userDTO) {
        return upsert(userDTO, findById(id));
    }

    @Override
    public void delete(Long id) {
        userRepository.delete(findById(id));
    }

    private UserDTO upsert(UserDTO userDTO, User user) {
        userMapper.map(userDTO, user);
        return userMapper.map(userRepository.save(user));
    }

    private User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new UserException(ErrorDTO.builder().message(String.format("User with id %s not found", id)).timestamp(new Date()).build()));
    }
}
