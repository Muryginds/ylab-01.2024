package io.ylab.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.dto.response.AuditionEventDto;
import io.ylab.dto.response.UserDto;
import io.ylab.enumerated.AuditionEventType;
import io.ylab.mapper.AuditionEventMapper;
import io.ylab.service.AuditionEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashSet;
import java.util.Set;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuditionEventControllerImplTest {

    @Mock
    private AuditionEventService auditionEventService;
    @Mock
    private AuditionEventMapper auditionEventMapper;
    @InjectMocks
    private AuditionEventControllerImpl auditionEventController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(auditionEventController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testGetEvents() throws Exception {
        long userId = 1L;
        var userDto = UserDto.builder().id(userId).name("John Doe").build();
        Set<AuditionEventDto> events = new HashSet<>();
        events.add(new AuditionEventDto(
                1L, userDto, AuditionEventType.SESSION_START,"Audition Event 1", "2020-10-20")
        );
        events.add(new AuditionEventDto(
                2L, userDto, AuditionEventType.SESSION_END,"Audition Event 2", "2020-10-21")
        );

        when(auditionEventService.getEvents(userId)).thenReturn(events);

        mockMvc.perform(get("/api/v1/events/all?userId=1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(events))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].message").value("Audition Event 2"))
                .andExpect(jsonPath("$[1].id").value(1))
                .andExpect(jsonPath("$[1].message").value("Audition Event 1"));
    }
}