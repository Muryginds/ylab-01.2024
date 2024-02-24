package io.ylab.backend.service;

import io.ylab.backend.dto.response.SubmissionDto;
import io.ylab.commons.entity.MeterReading;
import io.ylab.commons.entity.Submission;
import io.ylab.backend.mapper.SubmissionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionRepresentationServiceTest {
    @Mock
    private MeterReadingsService meterReadingsService;
    @Mock
    private SubmissionService submissionService;
    @Mock
    private SubmissionMapper submissionMapper;
    @InjectMocks
    private SubmissionRepresentationService submissionRepresentationService;

    @Test
    void testGetSubmissionDTO() {
        var date = LocalDate.now();
        var userId = 1L;
        var submission = Submission.builder().build();
        var meterReadings = new HashSet<MeterReading>();
        var expectedDto = SubmissionDto.builder().build();
        when(submissionService.getSubmission(date, userId)).thenReturn(submission);
        when(meterReadingsService.getBySubmission(submission)).thenReturn(meterReadings);
        when(submissionMapper.toSubmissionDTO(submission, meterReadings)).thenReturn(expectedDto);

        SubmissionDto result = submissionRepresentationService.getSubmissionDTO(date, userId);

        assertEquals(expectedDto, result);
    }

    @Test
    void testGetAllSubmissionDTOs() {
        var userId = 1L;
        var submission = Submission.builder().build();
        var submissions = Collections.singleton(submission);
        var meterReadings = new HashSet<MeterReading>();
        var expectedDto = SubmissionDto.builder().build();
        when(submissionService.getAll(userId)).thenReturn(submissions);
        when(meterReadingsService.getBySubmission(submission)).thenReturn(meterReadings);
        when(submissionMapper.toSubmissionDTO(submission, meterReadings)).thenReturn(expectedDto);

        var result = submissionRepresentationService.getAllSubmissionDTOs(userId);

        assertEquals(Collections.singleton(expectedDto), result);
    }
}
