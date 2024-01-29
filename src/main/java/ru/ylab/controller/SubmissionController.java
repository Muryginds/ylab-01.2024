package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.in.dto.SubmissionDTO;
import ru.ylab.in.dto.request.SubmissionByDateRequestDTO;
import ru.ylab.in.dto.request.SubmissionRequestDTO;
import ru.ylab.service.SubmissionService;

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
    private final SubmissionService submissionService;

    /**
     * Saves a new submission based on the provided submission request.
     *
     * @param request The submission request containing meter readings.
     */
    public void save(SubmissionRequestDTO request) {
        submissionService.save(request);
    }

    /**
     * Retrieves all submissions for a user with the specified ID.
     *
     * @param userId The ID of the user for whom submissions are retrieved.
     * @return A collection of SubmissionDTO representing the user's submissions.
     */
    public Collection<SubmissionDTO> getAllByUserId(Long userId) {
        return submissionService.getAllByUserId(userId);
    }

    /**
     * Retrieves a submission for a user based on the specified date.
     *
     * @param request The request containing the user ID and submission date.
     * @return The SubmissionDTO representing the user's submission on the specified date.
     */
    public SubmissionDTO getSubmissionByDate(SubmissionByDateRequestDTO request) {
        return submissionService.getSubmissionByDateAndUserId(request);
    }

    /**
     * Retrieves the last submission for a user with the specified ID.
     *
     * @param userId The ID of the user for whom the last submission is retrieved.
     * @return The SubmissionDTO representing the user's last submission.
     */
    public SubmissionDTO getLastSubmissionByUserId(Long userId) {
        return submissionService.getLastSubmissionByUserId(userId);
    }
}
