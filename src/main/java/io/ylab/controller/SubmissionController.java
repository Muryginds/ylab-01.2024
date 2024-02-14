package io.ylab.controller;

import lombok.RequiredArgsConstructor;
import io.ylab.dto.response.SubmissionDTO;
import io.ylab.dto.request.AllSubmissionsRequestDTO;
import io.ylab.dto.request.SubmissionRequestDTO;
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
     * @param request The request containing ID of the user for whom submissions are retrieved.
     * @return A collection of SubmissionDTO representing the user's submissions.
     */
    public Collection<SubmissionDTO> getAllSubmissionDTOs(AllSubmissionsRequestDTO request) {
        return submissionRepresentationService.getAllSubmissionDTOs(request);
    }

    /**
     * Retrieves a submission for a user based on the specified date.
     *
     * @param request The request containing the user ID and submission date.
     * @return The SubmissionDTO representing the user's submission on the specified date.
     */
    public SubmissionDTO getSubmissionDTO(SubmissionRequestDTO request) {
        return submissionRepresentationService.getSubmissionDTO(request);
    }
}
