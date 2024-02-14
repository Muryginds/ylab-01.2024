package io.ylab.service;

import lombok.RequiredArgsConstructor;
import io.ylab.dto.response.SubmissionDTO;
import io.ylab.dto.request.AllSubmissionsRequestDTO;
import io.ylab.dto.request.SubmissionRequestDTO;
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

    public SubmissionDTO getSubmissionDTO(SubmissionRequestDTO request) {
        var submission = submissionService.getSubmission(request);
        var meterReadings = meterReadingsService.getBySubmission(submission);
        return SubmissionMapper.MAPPER.toSubmissionDTO(submission, meterReadings);
    }

    public Collection<SubmissionDTO> getAllSubmissionDTOs(AllSubmissionsRequestDTO request) {
        var submissions = submissionService.getAll(request);
        var collection = new HashSet<SubmissionDTO>();
        for (var submission : submissions) {
            var meterReadings = meterReadingsService.getBySubmission(submission);
            collection.add(SubmissionMapper.MAPPER.toSubmissionDTO(submission, meterReadings));
        }
        return collection;
    }
}
