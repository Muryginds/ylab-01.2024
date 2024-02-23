package io.ylab.backend.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.backend.dto.request.UserAuthorizationRequestDto;
import io.ylab.backend.dto.response.MessageDto;
import io.ylab.backend.dto.response.UserDto;
import io.ylab.backend.exception.UserAuthenticationException;
import io.ylab.backend.exception.UserNotAuthorizedException;
import io.ylab.backend.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerImplTest {
    @Mock
    private LoginService loginService;
    @InjectMocks
    private LoginControllerImpl loginController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void authorize_ValidCredentials_ReturnsUserDto() throws Exception {
        var requestDto = new UserAuthorizationRequestDto("John Doe", "password");
        var userDto = UserDto.builder().id(1L).name("John Doe").build();
        when(loginService.authorize(any(UserAuthorizationRequestDto.class))).thenReturn(userDto);

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void authorize_InvalidCredentials_ReturnsUnauthorized() {
        var requestDto = new UserAuthorizationRequestDto("username", "password");
        when(loginService.authorize(any(UserAuthorizationRequestDto.class)))
                .thenThrow(new UserAuthenticationException());

        assertThrows(UserAuthenticationException.class, () -> loginController.authorize(requestDto));
    }

    @Test
    void logout_ValidRequest_ReturnsMessageDto() throws Exception {
        var messageDto = new MessageDto("User logged out");
        when(loginService.logout()).thenReturn(messageDto);

        mockMvc.perform(post("/api/v1/auth/logout")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void logout_UnauthorizedRequest_ReturnsUnauthorized() {
        when(loginService.logout()).thenThrow(new UserNotAuthorizedException());

        assertThrows(UserNotAuthorizedException.class, () -> loginController.logout());
    }
}
