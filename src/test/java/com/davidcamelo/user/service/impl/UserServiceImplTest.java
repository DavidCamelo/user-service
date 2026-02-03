package com.davidcamelo.user.service.impl;

import com.davidcamelo.user.dto.FilterDTO;
import com.davidcamelo.user.dto.UserDTO;
import com.davidcamelo.user.entity.User;
import com.davidcamelo.user.error.UserNotFoundException;
import com.davidcamelo.user.repository.UserRepository;
import com.davidcamelo.user.util.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .build();

        userDTO = UserDTO.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .build();
    }

    @Test
    void testCreate_Success() {
        // Given
        var inputDTO = UserDTO.builder()
                .name("John")
                .lastName("Doe")
                .build();

        var newUser = new User();
        var savedUser = User.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(savedUser);
        when(userMapper.map(savedUser)).thenReturn(userDTO);
        doNothing().when(userMapper).map(inputDTO, newUser);

        // When
        var result = userService.create(inputDTO);

        // Then
        assertNotNull(result);
        assertEquals("John", result.name());
        assertEquals("Doe", result.lastName());
        verify(userRepository, times(1)).save(any(User.class));
        verify(userMapper, times(1)).map(any(User.class));
    }

    @Test
    void testGetById_Success() {
        // Given
        var userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userMapper.map(user)).thenReturn(userDTO);

        // When
        var result = userService.getById(userId);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("John", result.name());
        assertEquals("Doe", result.lastName());
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, times(1)).map(user);
    }

    @Test
    void testGetById_UserNotFound() {
        // Given
        var userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.getById(userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userMapper, never()).map(any(User.class));
    }

    @Test
    void testGetAll_WithPagination() {
        // Given
        var filterDTO = new FilterDTO();
        filterDTO.setPageNumber(0);
        filterDTO.setPageSize(10);
        filterDTO.setSortBy("id");
        filterDTO.setSortDirection(Sort.Direction.ASC);

        var users = Collections.singletonList(user);
        var userPage = new PageImpl<>(users, PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))), 1);
        var dtoPage = new PageImpl<>(Collections.singletonList(userDTO),
                PageRequest.of(0, 10, Sort.by(Sort.Order.asc("id"))), 1);

        when(userRepository.findAll(any(PageRequest.class))).thenReturn(userPage);
        when(userMapper.mapPage(userPage)).thenReturn(dtoPage);

        // When
        var result = userService.getAll(filterDTO);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(1, result.getContent().size());
        assertEquals("John", result.getContent().getFirst().name());
        verify(userRepository, times(1)).findAll(any(PageRequest.class));
        verify(userMapper, times(1)).mapPage(userPage);
    }

    @Test
    void testGetAll_WithoutPagination() {
        // Given
        var filterDTO = new FilterDTO();
        filterDTO.setPageNumber(null);
        filterDTO.setPageSize(null);

        var user2 = User.builder().id(2L).name("Jane").lastName("Smith").build();
        var users = Arrays.asList(user, user2);

        var userDTO2 = UserDTO.builder().id(2L).name("Jane").lastName("Smith").build();

        when(userRepository.findAll()).thenReturn(users);
        when(userMapper.map(user)).thenReturn(userDTO);
        when(userMapper.map(user2)).thenReturn(userDTO2);

        // When
        var result = userService.getAll(filterDTO);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(2)).map(any(User.class));
    }

    @Test
    void testUpdate_Success() {
        // Given
        var userId = 1L;
        var updateDTO = UserDTO.builder()
                .name("UpdatedName")
                .lastName("UpdatedLastName")
                .build();

        var updatedUser = User.builder()
                .id(1L)
                .name("UpdatedName")
                .lastName("UpdatedLastName")
                .build();

        var updatedDTO = UserDTO.builder()
                .id(1L)
                .name("UpdatedName")
                .lastName("UpdatedLastName")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);
        when(userMapper.map(updatedUser)).thenReturn(updatedDTO);
        doNothing().when(userMapper).map(updateDTO, user);

        // When
        var result = userService.update(userId, updateDTO);

        // Then
        assertNotNull(result);
        assertEquals("UpdatedName", result.name());
        assertEquals("UpdatedLastName", result.lastName());
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdate_UserNotFound() {
        // Given
        var userId = 999L;
        var updateDTO = UserDTO.builder()
                .name("UpdatedName")
                .lastName("UpdatedLastName")
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.update(userId, updateDTO));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testDelete_Success() {
        // Given
        var userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        // When
        userService.delete(userId);

        // Then
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    void testDelete_UserNotFound() {
        // Given
        var userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UserNotFoundException.class, () -> userService.delete(userId));
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, never()).delete(any(User.class));
    }
}
