package io.ylab.backend.service;

import io.ylab.backend.mapper.SubmissionMapper;
import io.ylab.backend.dto.response.SubmissionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Service responsible for representing submissions with meter readings.
 */
@Service
@RequiredArgsConstructor
public class SubmissionRepresentationService {
    private final MeterReadingsService meterReadingsService;
    private final SubmissionService submissionService;
    private final SubmissionMapper submissionMapper;

    @Transactional
    public SubmissionDto getSubmissionDTO(LocalDate date, Long userId) {
        var submission = submissionService.getSubmission(date, userId);
        var meterReadings = meterReadingsService.getBySubmission(submission);
        return submissionMapper.toSubmissionDTO(submission, meterReadings);
    }

    @Transactional
    public Set<SubmissionDto> getAllSubmissionDTOs(Long userId) {
        var submissions = submissionService.getAll(userId);
        var collection = new HashSet<SubmissionDto>();
        for (var submission : submissions) {
            var meterReadings = meterReadingsService.getBySubmission(submission);
            collection.add(submissionMapper.toSubmissionDTO(submission, meterReadings));
        }
        return collection;
    }
}
