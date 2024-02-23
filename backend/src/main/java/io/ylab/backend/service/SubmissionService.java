package io.ylab.backend.service;

import io.ylab.backend.annotation.Auditable;
import io.ylab.backend.entity.Submission;
import io.ylab.backend.enumerated.AuditionEventType;
import io.ylab.backend.enumerated.UserRole;
import io.ylab.backend.exception.NoPermissionException;
import io.ylab.backend.exception.NoSubmissionException;
import io.ylab.backend.exception.UserNotFoundException;
import io.ylab.backend.mapper.SubmissionMapper;
import io.ylab.backend.repository.SubmissionRepository;
import io.ylab.backend.utils.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The SubmissionService class provides functionality related to user submissions and meter readings.
 * It includes methods for retrieving submission history, finding the last submission,
 * saving new submissions, and other submission-related operations.
 * This service interacts with the SubmissionRepository,
 * UserService, and AuditionEventService to perform various submission-related tasks.
 */
@Service
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final UserService userService;
    private final SubmissionMapper submissionMapper;

    /**
     * Retrieves all submissions for a given user based on user ID.
     *
     * @param userId containing ID of the user for whom submissions are retrieved.
     * @return Collection of Submission entities representing the user's submission history.
     * @throws NoPermissionException                   If the current user does not have permission to access the history.
     * @throws UserNotFoundException If user is not found with given ID.
     */
    @Transactional
    @Auditable(eventType = AuditionEventType.SUBMISSION_HISTORY_ACQUIRE)
    public Collection<Submission> getAll(Long userId) {
        var currentUser = CurrentUserUtils.getCurrentUser();
        var targetUser = userId == null ? currentUser : userService.getUserById(userId);
        if (!(currentUser.equals(targetUser) || currentUser.getRole().equals(UserRole.ADMIN))) {
            throw new NoPermissionException();
        }
        var submissionModels = submissionRepository.getByUserId(targetUser.getId());
        return submissionModels.stream()
                .map(sm -> submissionMapper.toSubmission(sm, targetUser))
                .collect(Collectors.toSet());
    }

    /**
     * Retrieves a submission for a given submission ID.
     *
     * @param submissionId Containing submission ID.
     * @return Submission by given ID.
     * @throws NoSubmissionException If no submissions with given ID exists.
     */
    @Transactional
    public Submission getSubmissionById(Long submissionId) {
        var submissionModel = submissionRepository.getById(submissionId)
                .orElseThrow(() -> new NoSubmissionException(submissionId));
        var user = userService.getUserById(submissionModel.userId());
        return submissionMapper.toSubmission(submissionModel, user);
    }

    /**
     * Retrieves a submission for a given user and date.
     *
     * @param date request containing submission date.
     * @param userId containing the user ID.
     * @return Submission for the user and date.
     * @throws NoPermissionException If the current user does not have permission to access the submission.
     * @throws NoSubmissionException If no submissions are found for the user and date.
     */
    @Transactional
    @Auditable(eventType = AuditionEventType.SINGLE_SUBMISSION_ACQUIRE)
    public Submission getSubmission(LocalDate date, Long userId) {
        var currentUser = CurrentUserUtils.getCurrentUser();
        var targetUser = userId == null ? currentUser : userService.getUserById(userId);
        if (!(currentUser.equals(targetUser) || currentUser.getRole().equals(UserRole.ADMIN))) {
            throw new NoPermissionException();
        }

        Optional<Submission> submissionOptional;

        if (date == null) {
            submissionOptional = findLastSubmissionByUserId(targetUser.getId());
        } else {
            submissionOptional = findByUserIdAndDate(targetUser.getId(), date);
        }

        return submissionOptional
                .orElseThrow(() -> new NoSubmissionException(targetUser.getName()));
    }

    private Optional<Submission> findLastSubmissionByUserId(Long userId) {
        var submissionModelOptional
                = submissionRepository.findLastSubmissionByUserId(userId);
        if (submissionModelOptional.isPresent()) {
            var user = userService.getUserById(userId);
            var submission = submissionMapper.toSubmission(submissionModelOptional.get(), user);
            return Optional.of(submission);
        }
        return Optional.empty();
    }

    private Optional<Submission> findByUserIdAndDate(Long userId, LocalDate date) {
        var submissionModelOptional = submissionRepository.findSubmissionByUserIdAndDate(userId, date);
        if (submissionModelOptional.isPresent()) {
            var user = userService.getUserById(userId);
            var submission = submissionMapper.toSubmission(submissionModelOptional.get(), user);
            return Optional.of(submission);
        }
        return Optional.empty();
    }

    /**
     * Saves a single submission.
     *
     * @param submission The meter to be saved.
     */
    public void save(Submission submission) {
        submissionRepository.save(submission);
    }

    public boolean checkExistsByUserIdAndDate(Long id, LocalDate date) {
        return submissionRepository.checkExistsByUserIdAndDate(id, date);
    }
}
