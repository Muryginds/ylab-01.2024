package io.ylab.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.ylab.dto.request.NewReadingsSubmissionRequestDto;
import io.ylab.dto.response.MessageDto;
import io.ylab.dto.response.SubmissionDto;
import io.ylab.service.ReadingsRecordingService;
import io.ylab.service.SubmissionRepresentationService;
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

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
        Set<SubmissionDto> submissionDtoSet = new HashSet<>();

        when(submissionRepresentationService.getAllSubmissionDTOs(any(Long.class))).thenReturn(submissionDtoSet);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/submissions/all")
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getSubmissionDTO_ValidRequest_ReturnsSubmissionDto() throws Exception {
        SubmissionDto submissionDto = SubmissionDto.builder().build();
        LocalDate date = LocalDate.now();
        when(submissionRepresentationService.getSubmissionDTO(any(LocalDate.class), any(Long.class))).thenReturn(submissionDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/submissions")
                        .param("date", date.toString())
                        .param("userId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void saveNewSubmission_ValidRequest_ReturnsMessageDto() throws Exception {
        NewReadingsSubmissionRequestDto requestDto = NewReadingsSubmissionRequestDto.builder().build();
        MessageDto messageDto = new MessageDto("Submission saved successfully");
        when(readingsRecordingService.saveNewSubmission(any(NewReadingsSubmissionRequestDto.class))).thenReturn(messageDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/submissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));
    }
}
