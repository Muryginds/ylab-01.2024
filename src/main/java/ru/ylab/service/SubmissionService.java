package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.dto.request.AllSubmissionsRequestDTO;
import ru.ylab.dto.request.SubmissionRequestDTO;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.entity.Submission;
import ru.ylab.entity.User;
import ru.ylab.enumerated.AuditionEventType;
import ru.ylab.enumerated.UserRole;
import ru.ylab.exception.NoPermissionException;
import ru.ylab.exception.NoSubmissionException;
import ru.ylab.mapper.SubmissionMapper;
import ru.ylab.repository.SubmissionRepository;

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
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final UserService userService;
    private final AuditionEventService auditionEventService;

    /**
     * Retrieves all submissions for a given user based on user ID.
     * Audits the action and logs an audition event.
     *
     * @param request The request containing ID of the user for whom submissions are retrieved.
     * @return Collection of Submission entities representing the user's submission history.
     * @throws NoPermissionException                   If the current user does not have permission to access the history.
     * @throws ru.ylab.exception.UserNotFoundException If user is not found with given ID.
     */
    public Collection<Submission> getAll(AllSubmissionsRequestDTO request) {
        var userId = request.userId();
        var currentUser = userService.getCurrentUser();
        var targetUser = userId == null ? currentUser : userService.getUserById(userId);
        if (!(currentUser.equals(targetUser) || currentUser.getRole().equals(UserRole.ADMIN))) {
            throw new NoPermissionException();
        }
        var auditionMessage = String.format("Submission history acquired for user id '%s'", targetUser.getId());
        var eventType = AuditionEventType.SUBMISSION_HISTORY_ACQUIRE;
        saveNewSubmissionAuditionEvent(currentUser, eventType, auditionMessage);
        var submissionModels = submissionRepository.getByUserId(targetUser.getId());
        return submissionModels.stream()
                .map(sm -> SubmissionMapper.MAPPER.toSubmission(sm, targetUser))
                .collect(Collectors.toSet());
    }

    private void saveNewSubmissionAuditionEvent(User currentUser, AuditionEventType eventType, String auditionMessage) {
        var event = AuditionEvent.builder()
                .user(currentUser)
                .eventType(eventType)
                .message(auditionMessage)
                .build();
        auditionEventService.save(event);
    }

    /**
     * Retrieves a submission for a given submission ID.
     *
     * @param submissionId Containing submission ID.
     * @return Submission by given ID.
     * @throws NoSubmissionException If no submissions with given ID exists.
     */
    public Submission getSubmissionById(Long submissionId) {
        var submissionModel = submissionRepository.getById(submissionId)
                .orElseThrow(() -> new NoSubmissionException(submissionId));
        var user = userService.getUserById(submissionModel.userId());
        return SubmissionMapper.MAPPER.toSubmission(submissionModel, user);
    }

    /**
     * Retrieves a submission for a given user and date.
     * Audits the action and logs an audition event.
     *
     * @param request The request containing user ID and submission date.
     * @return Submission for the user and date.
     * @throws NoPermissionException If the current user does not have permission to access the submission.
     * @throws NoSubmissionException If no submissions are found for the user and date.
     */
    public Submission getSubmission(SubmissionRequestDTO request) {
        var userId = request.userId();
        var date = request.date();
        var currentUser = userService.getCurrentUser();
        var targetUser = userId == null ? currentUser : userService.getUserById(userId);
        if (!(currentUser.equals(targetUser) || currentUser.getRole().equals(UserRole.ADMIN))) {
            throw new NoPermissionException();
        }

        var auditionMessage = String.format("Submission acquired for user id '%s'", targetUser.getId());
        var eventType = AuditionEventType.SINGLE_SUBMISSION_ACQUIRE;
        saveNewSubmissionAuditionEvent(currentUser, eventType, auditionMessage);

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
            var submission = SubmissionMapper.MAPPER.toSubmission(submissionModelOptional.get(), user);
            return Optional.of(submission);
        }
        return Optional.empty();
    }

    private Optional<Submission> findByUserIdAndDate(Long userId, LocalDate date) {
        var submissionModelOptional = submissionRepository.findSubmissionByUserIdAndDate(userId, date);
        if (submissionModelOptional.isPresent()) {
            var user = userService.getUserById(userId);
            var submission = SubmissionMapper.MAPPER.toSubmission(submissionModelOptional.get(), user);
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
