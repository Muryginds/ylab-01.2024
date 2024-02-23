package io.ylab.backend.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.backend.dto.request.NewReadingsSubmissionRequestDto;
import io.ylab.backend.dto.request.ReadingRequestDto;
import io.ylab.backend.dto.response.MessageDto;
import io.ylab.backend.dto.response.SubmissionDto;
import io.ylab.backend.service.ReadingsRecordingService;
import io.ylab.backend.service.SubmissionRepresentationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class SubmissionControllerImplTest {
    @Mock
    private SubmissionRepresentationService submissionRepresentationService;
    @Mock
    private ReadingsRecordingService readingsRecordingService;
    @InjectMocks
    private SubmissionControllerImpl submissionController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(submissionController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void getAllSubmissionDTOs_ValidRequest_ReturnsSubmissionDtoSet() throws Exception {
        var submissionDtoSet = new HashSet<SubmissionDto>();

        when(submissionRepresentationService.getAllSubmissionDTOs(any(Long.class))).thenReturn(submissionDtoSet);

        mockMvc.perform(get("/api/v1/submissions/all")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getSubmissionDTO_ValidRequest_ReturnsSubmissionDto() throws Exception {
        var submissionDto = SubmissionDto.builder().build();
        var date = LocalDate.now();
        when(submissionRepresentationService.getSubmissionDTO(any(LocalDate.class), any(Long.class))).thenReturn(submissionDto);

        mockMvc.perform(get("/api/v1/submissions")
                        .param("date", date.toString())
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void saveNewSubmission_ValidRequest_ReturnsMessageDto() throws Exception {
        var meterReadings = List.of(ReadingRequestDto.builder().meterId(1L).value(100L).build());
        var requestDto = NewReadingsSubmissionRequestDto.builder().meterReadings(meterReadings).build();
        var messageDto = new MessageDto("Submission saved successfully");
        when(readingsRecordingService.saveNewSubmission(any(NewReadingsSubmissionRequestDto.class))).thenReturn(messageDto);

        mockMvc.perform(post("/api/v1/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
}
