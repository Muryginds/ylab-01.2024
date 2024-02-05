package ru.ylab.repository;

import ru.ylab.entity.Submission;
import ru.ylab.model.SubmissionModel;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

/**
 * Repository interface for managing submission-related data operations.
 */
public interface SubmissionRepository {

    /**
     * Gets all submissions associated with a specific user ID.
     *
     * @param userId The ID of the user.
     * @return A collection of submission models for the specified user.
     */
    Collection<SubmissionModel> getByUserId(Long userId);

    /**
     * Finds a submission by submission ID in the repository.
     *
     * @param submissionId The ID of submission.
     * @return An optional containing the found submission model or empty if not found.
     */
    Optional<SubmissionModel> getById(Long submissionId);

    /**
     * Saves a submission in the repository.
     *
     * @param submission The submission to be saved.
     */
    void save(Submission submission);

    /**
     * Checks if a submission exists for a given user ID and date.
     *
     * @param userId The ID of the user.
     * @param date   The date of the submission.
     * @return True if a submission exists, false otherwise.
     */
    boolean checkExistsByUserIdAndDate(Long userId, LocalDate date);

    /**
     * Finds a submission by user ID and date in the repository.
     *
     * @param userId The ID of the user.
     * @param date   The date of the submission.
     * @return An optional containing the found submission model or empty if not found.
     */
    Optional<SubmissionModel> findSubmissionByUserIdAndDate(Long userId, LocalDate date);

    /**
     * Finds the last submission by user ID in the repository.
     *
     * @param userId The ID of the user.
     * @return An optional containing the last found submission model or empty if not found.
     */
    Optional<SubmissionModel> findLastSubmissionByUserId(Long userId);
}
