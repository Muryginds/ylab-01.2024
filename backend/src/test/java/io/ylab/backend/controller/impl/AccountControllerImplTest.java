package io.ylab.backend.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.backend.dto.request.UserRegistrationRequestDto;
import io.ylab.backend.dto.response.UserDto;
import io.ylab.backend.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AccountControllerImplTest {
    @Mock
    private AccountService accountService;
    @InjectMocks
    private AccountControllerImpl accountController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(accountController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testRegisterUser() throws Exception {
        var requestDto = UserRegistrationRequestDto.builder()
                .name("John Doe")
                .password("password123")
                .build();
        var userDto = UserDto.builder().id(1L).name("John Doe").build();

        when(accountService.registerUser(requestDto)).thenReturn(userDto);

        mockMvc.perform(post("/api/v1/accounts/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    void testGetCurrentUser() throws Exception {
        var userDto = UserDto.builder().id(1L).name("Jane Smith").build();

        when(accountService.getCurrentUserDto()).thenReturn(userDto);

        mockMvc.perform(get("/api/v1/accounts/me")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Smith"));
    }
}