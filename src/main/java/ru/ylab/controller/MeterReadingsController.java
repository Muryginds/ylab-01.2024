package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.dto.MeterReadingDTO;
import ru.ylab.service.MeterReadingsService;

import java.util.Set;

/**
 * Controller class for handling meter readings-related operations and interactions.
 *
 * <p>This class acts as an interface between the user interface and the underlying business logic
 * related to meter readings.
 */
@RequiredArgsConstructor
public class MeterReadingsController {
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
    public Set<MeterReadingDTO> getAllBySubmissionId(Long submissionId) {
        return meterReadingsService.getAllBySubmissionId(submissionId);
    }
}
