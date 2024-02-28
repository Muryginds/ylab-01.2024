package io.ylab.backend.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.backend.dto.request.NewMeterTypeRequestDto;
import io.ylab.backend.dto.response.MessageDto;
import io.ylab.backend.exception.MeterTypeExistException;
import io.ylab.backend.service.MeterTypeService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MeterTypeControllerImplTest {
    @Mock
    private MeterTypeService meterTypeService;

    @InjectMocks
    private MeterTypeControllerImpl meterTypeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(meterTypeController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void save_ValidRequest_ReturnsMessageDto() throws Exception {
        var requestDto = new NewMeterTypeRequestDto("Electricity");
        var messageDto = new MessageDto("Meter type saved successfully");
        when(meterTypeService.save(any(NewMeterTypeRequestDto.class))).thenReturn(messageDto);

        mockMvc.perform(post("/api/v1/meter-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void save_DuplicateMeterType_ThrowsMeterTypeExistException() {
        var requestDto = new NewMeterTypeRequestDto("Electricity");
        when(meterTypeService.save(any(NewMeterTypeRequestDto.class)))
                .thenThrow(new MeterTypeExistException("Meter type already exists"));

        assertThrows(MeterTypeExistException.class, () -> meterTypeController.save(requestDto));
    }
}
