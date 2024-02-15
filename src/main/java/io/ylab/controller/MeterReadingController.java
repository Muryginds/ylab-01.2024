package io.ylab.controller;

import io.ylab.dto.response.MeterReadingDto;
import lombok.RequiredArgsConstructor;
import io.ylab.service.MeterReadingsService;

import java.util.Set;

/**
 * Controller class for handling meter readings-related operations and interactions.
 *
 * <p>This class acts as an interface between the user interface and the underlying business logic
 * related to meter readings.
 */
@RequiredArgsConstructor
public class MeterReadingController {
    /**
     * The associated service for meter readings-related operations.
     */
    private final MeterReadingsService meterReadingsService;

    /**
     * Retrieves all meter readings associated with a specific submission.
     *
     * @param submissionId The ID of the submission for which to retrieve meter readings.
     * @return A set of meter reading DTOs associated with the specified submission.
     */
    public Set<MeterReadingDto> getAllBySubmissionId(Long submissionId) {
        return meterReadingsService.getAllBySubmissionId(submissionId);
    }
}
