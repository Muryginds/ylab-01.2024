package ru.ylab.repository;

import ru.ylab.entity.Submission;

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
     * @return A collection of submissions for the specified user.
     */
    Collection<Submission> getByUserId(Long userId);

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
     * @return An optional containing the found submission or empty if not found.
     */
    Optional<Submission> findByUserIdAndDate(Long userId, LocalDate date);

    /**
     * Finds the last submission by user ID in the repository.
     *
     * @param userId The ID of the user.
     * @return An optional containing the last found submission or empty if not found.
     */
    Optional<Submission> findLastByUserId(Long userId);
}
