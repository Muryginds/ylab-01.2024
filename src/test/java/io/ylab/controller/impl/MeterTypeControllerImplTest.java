package io.ylab.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.dto.request.NewMeterTypeRequestDto;
import io.ylab.dto.response.MessageDto;
import io.ylab.exception.MeterTypeExistException;
import io.ylab.service.MeterTypeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        NewMeterTypeRequestDto requestDto = new NewMeterTypeRequestDto("Electricity");
        MessageDto messageDto = new MessageDto("Meter type saved successfully");
        when(meterTypeService.save(any(NewMeterTypeRequestDto.class))).thenReturn(messageDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/meter-types")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void save_DuplicateMeterType_ThrowsMeterTypeExistException() {
        NewMeterTypeRequestDto requestDto = new NewMeterTypeRequestDto("Electricity");
        when(meterTypeService.save(any(NewMeterTypeRequestDto.class)))
                .thenThrow(new MeterTypeExistException("Meter type already exists"));

        assertThrows(MeterTypeExistException.class, () -> meterTypeController.save(requestDto));
    }
}
