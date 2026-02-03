package com.davidcamelo.user.api;

import com.davidcamelo.user.dto.ErrorDTO;
import com.davidcamelo.user.dto.FilterDTO;
import com.davidcamelo.user.dto.UserDTO;
import com.davidcamelo.user.error.UserControllerAdvice;
import com.davidcamelo.user.error.UserNotFoundException;
import com.davidcamelo.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@Import(UserControllerAdvice.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = UserDTO.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .build();
    }

    @Test
    void testCreate_Success() throws Exception {
        // Given
        var inputDTO = UserDTO.builder()
                .name("John")
                .lastName("Doe")
                .build();

        when(userService.create(any(UserDTO.class))).thenReturn(userDTO);

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));

        verify(userService, times(1)).create(any(UserDTO.class));
    }

    @Test
    void testGetById_Success() throws Exception {
        // Given
        var userId = 1L;
        when(userService.getById(userId)).thenReturn(userDTO);

        // When & Then
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("John")))
                .andExpect(jsonPath("$.lastName", is("Doe")));

        verify(userService, times(1)).getById(userId);
    }

    @Test
    void testGetById_NotFound() throws Exception {
        // Given
        var userId = 999L;
        when(userService.getById(userId)).thenThrow(new UserNotFoundException(ErrorDTO.builder().build()));

        // When & Then
        mockMvc.perform(get("/api/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).getById(userId);
    }

    @Test
    void testGetAll_Success() throws Exception {
        // Given
        var user2 = UserDTO.builder().id(2L).name("Jane").lastName("Smith").build();
        var users = Arrays.asList(userDTO, user2);
        var page = new PageImpl<>(users, PageRequest.of(0, 10), 2);

        when(userService.getAll(any(FilterDTO.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/users")
                .param("pageNumber", "0")
                .param("pageSize", "10")
                .param("sortBy", "id")
                .param("sortDirection", "ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].id", is(1)))
                .andExpect(jsonPath("$.content[0].name", is("John")))
                .andExpect(jsonPath("$.content[1].id", is(2)))
                .andExpect(jsonPath("$.content[1].name", is("Jane")))
                .andExpect(jsonPath("$.totalElements", is(2)));

        verify(userService, times(1)).getAll(any(FilterDTO.class));
    }

    @Test
    void testGetAll_WithoutPagination() throws Exception {
        // Given
        var users = Collections.singletonList(userDTO);
        var page = new PageImpl<>(users);

        when(userService.getAll(any(FilterDTO.class))).thenReturn(page);

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("John")));

        verify(userService, times(1)).getAll(any(FilterDTO.class));
    }

    @Test
    void testUpdate_Success() throws Exception {
        // Given
        var userId = 1L;
        var updateDTO = UserDTO.builder()
                .name("UpdatedName")
                .lastName("UpdatedLastName")
                .build();

        var updatedDTO = UserDTO.builder()
                .id(userId)
                .name("UpdatedName")
                .lastName("UpdatedLastName")
                .build();

        when(userService.update(eq(userId), any(UserDTO.class))).thenReturn(updatedDTO);

        // When & Then
        mockMvc.perform(put("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.name", is("UpdatedName")))
                .andExpect(jsonPath("$.lastName", is("UpdatedLastName")));

        verify(userService, times(1)).update(eq(userId), any(UserDTO.class));
    }

    @Test
    void testUpdate_NotFound() throws Exception {
        // Given
        var userId = 999L;
        var updateDTO = UserDTO.builder()
                .name("UpdatedName")
                .lastName("UpdatedLastName")
                .build();

        when(userService.update(eq(userId), any(UserDTO.class)))
                .thenThrow(new UserNotFoundException(ErrorDTO.builder().build()));

        // When & Then
        mockMvc.perform(put("/api/users/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).update(eq(userId), any(UserDTO.class));
    }

    @Test
    void testDelete_Success() throws Exception {
        // Given
        var userId = 1L;
        doNothing().when(userService).delete(userId);

        // When & Then
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isOk());

        verify(userService, times(1)).delete(userId);
    }

    @Test
    void testDelete_NotFound() throws Exception {
        // Given
        var userId = 999L;
        doThrow(new UserNotFoundException(ErrorDTO.builder().build())).when(userService).delete(userId);

        // When & Then
        mockMvc.perform(delete("/api/users/{id}", userId))
                .andExpect(status().isNotFound());

        verify(userService, times(1)).delete(userId);
    }
}
