package com.davidcamelo.user.util.mapper.impl;

import com.davidcamelo.user.dto.UserDTO;
import com.davidcamelo.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserMapperImplTest {

    private UserMapperImpl userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapperImpl();
    }

    @Test
    void testMapUserToUserDTO() {
        // Given
        var user = User.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .build();

        // When
        var result = userMapper.map(user);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("John", result.name());
        assertEquals("Doe", result.lastName());
    }

    @Test
    void testMapUserToUserDTO_NullUser() {
        // When
        var result = userMapper.map(null);

        // Then
        assertNull(result);
    }

    @Test
    void testMapUserDTOToUser() {
        // Given
        var userDTO = UserDTO.builder()
                .name("Jane")
                .lastName("Smith")
                .build();
        var user = new User();

        // When
        userMapper.map(userDTO, user);

        // Then
        assertEquals("Jane", user.getName());
        assertEquals("Smith", user.getLastName());
    }

    @Test
    void testMapUserDTOToUser_UpdateExistingUser() {
        // Given
        var userDTO = UserDTO.builder()
                .name("UpdatedName")
                .lastName("UpdatedLastName")
                .build();
        var user = User.builder()
                .id(5L)
                .name("OldName")
                .lastName("OldLastName")
                .build();

        // When
        userMapper.map(userDTO, user);

        // Then
        assertEquals(5L, user.getId()); // ID should remain unchanged
        assertEquals("UpdatedName", user.getName());
        assertEquals("UpdatedLastName", user.getLastName());
    }

    @Test
    void testMapPage() {
        // Given
        var user1 = User.builder().id(1L).name("John").lastName("Doe").build();
        var user2 = User.builder().id(2L).name("Jane").lastName("Smith").build();
        var users = Arrays.asList(user1, user2);
        var userPage = new PageImpl<>(users, PageRequest.of(0, 10), 2);

        // When
        var result = userMapper.mapPage(userPage);

        // Then
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());

        var dto1 = result.getContent().getFirst();
        assertEquals(1L, dto1.id());
        assertEquals("John", dto1.name());
        assertEquals("Doe", dto1.lastName());

        var dto2 = result.getContent().get(1);
        assertEquals(2L, dto2.id());
        assertEquals("Jane", dto2.name());
        assertEquals("Smith", dto2.lastName());
    }

    @Test
    void testMapPage_EmptyPage() {
        // Given
        Page<User> emptyPage = new PageImpl<>(List.of(), PageRequest.of(0, 10), 0);

        // When
        var result = userMapper.mapPage(emptyPage);

        // Then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }
}
