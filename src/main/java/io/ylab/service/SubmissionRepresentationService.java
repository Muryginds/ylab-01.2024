package io.ylab.service;

import io.ylab.dto.response.SubmissionDto;
import lombok.RequiredArgsConstructor;
import io.ylab.dto.request.AllSubmissionsRequestDto;
import io.ylab.dto.request.SubmissionRequestDto;
import io.ylab.mapper.SubmissionMapper;

import java.util.Collection;
import java.util.HashSet;

/**
 * Service responsible for representing submissions with meter readings.
 */
@RequiredArgsConstructor
public class SubmissionRepresentationService {
    private final MeterReadingsService meterReadingsService;
    private final SubmissionService submissionService;

    public SubmissionDto getSubmissionDTO(SubmissionRequestDto requestDto) {
        var submission = submissionService.getSubmission(requestDto);
        var meterReadings = meterReadingsService.getBySubmission(submission);
        return SubmissionMapper.MAPPER.toSubmissionDTO(submission, meterReadings);
    }

    public Collection<SubmissionDto> getAllSubmissionDTOs(AllSubmissionsRequestDto requestDto) {
        var submissions = submissionService.getAll(requestDto);
        var collection = new HashSet<SubmissionDto>();
        for (var submission : submissions) {
            var meterReadings = meterReadingsService.getBySubmission(submission);
            collection.add(SubmissionMapper.MAPPER.toSubmissionDTO(submission, meterReadings));
        }
        return collection;
    }
}
