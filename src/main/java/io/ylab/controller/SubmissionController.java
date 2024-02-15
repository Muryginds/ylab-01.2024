package io.ylab.controller;

import io.ylab.dto.response.SubmissionDto;
import lombok.RequiredArgsConstructor;
import io.ylab.dto.request.AllSubmissionsRequestDto;
import io.ylab.dto.request.SubmissionRequestDto;
import io.ylab.service.SubmissionRepresentationService;

import java.util.Collection;

/**
 * Controller class for handling submission-related operations and interactions.
 *
 * <p>This class acts as an interface between the user interface and the underlying business logic
 * related to user submissions.
 */
@RequiredArgsConstructor
public class SubmissionController {
    /**
     * The associated service for submission-related operations.
     */
    private final SubmissionRepresentationService submissionRepresentationService;

    /**
     * Retrieves all submissions for a user with the specified ID.
     *
     * @param requestDto The request containing ID of the user for whom submissions are retrieved.
     * @return A collection of SubmissionDTO representing the user's submissions.
     */
    public Collection<SubmissionDto> getAllSubmissionDTOs(AllSubmissionsRequestDto requestDto) {
        return submissionRepresentationService.getAllSubmissionDTOs(requestDto);
    }

    /**
     * Retrieves a submission for a user based on the specified date.
     *
     * @param requestDto The request containing the user ID and submission date.
     * @return The SubmissionDTO representing the user's submission on the specified date.
     */
    public SubmissionDto getSubmissionDTO(SubmissionRequestDto requestDto) {
        return submissionRepresentationService.getSubmissionDTO(requestDto);
    }
}
