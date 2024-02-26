package io.ylab.service;

import io.ylab.dto.response.SubmissionDto;
import io.ylab.entity.MeterReading;
import io.ylab.entity.Submission;
import io.ylab.mapper.SubmissionMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;

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
        LocalDate date = LocalDate.now();
        Long userId = 1L;
        Submission submission = Submission.builder().build();
        Set<MeterReading> meterReadings = Collections.emptySet();
        SubmissionDto expectedDto = SubmissionDto.builder().build();
        when(submissionService.getSubmission(date, userId)).thenReturn(submission);
        when(meterReadingsService.getBySubmission(submission)).thenReturn(meterReadings);
        when(submissionMapper.toSubmissionDTO(submission, meterReadings)).thenReturn(expectedDto);

        SubmissionDto result = submissionRepresentationService.getSubmissionDTO(date, userId);

        assertEquals(expectedDto, result);
    }

    @Test
    void testGetAllSubmissionDTOs() {
        Long userId = 1L;
        Submission submission = Submission.builder().build();
        Set<Submission> submissions = Collections.singleton(submission);
        Set<MeterReading> meterReadings = Collections.emptySet();
        SubmissionDto expectedDto = SubmissionDto.builder().build();
        when(submissionService.getAll(userId)).thenReturn(submissions);
        when(meterReadingsService.getBySubmission(submission)).thenReturn(meterReadings);
        when(submissionMapper.toSubmissionDTO(submission, meterReadings)).thenReturn(expectedDto);

        Set<SubmissionDto> result = submissionRepresentationService.getAllSubmissionDTOs(userId);

        assertEquals(Collections.singleton(expectedDto), result);
    }
}
