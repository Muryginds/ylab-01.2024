package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.dto.SubmissionDTO;
import ru.ylab.dto.request.SubmissionByDateRequestDTO;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.entity.Submission;
import ru.ylab.enumerated.AuditionEventType;
import ru.ylab.enumerated.UserRole;
import ru.ylab.exception.NoPermissionException;
import ru.ylab.exception.NoSubmissionException;
import ru.ylab.mapper.SubmissionMapper;
import ru.ylab.repository.SubmissionRepository;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

/**
 * The SubmissionService class provides functionality related to user submissions and meter readings.
 * It includes methods for retrieving submission history, finding the last submission,
 * saving new submissions, and other submission-related operations.
 * This service interacts with the SubmissionRepository, MeterReadingsService, MeterService,
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
     * @param userId The ID of the user to retrieve submissions for.
     * @return Collection of SubmissionDTO representing the user's submission history.
     * @throws NoPermissionException If the current user does not have permission to access the history.
     */
    public Collection<SubmissionDTO> getAllByUserId(Long userId) {
        if (!(checkUserIsCurrentUser(userId) || checkCurrentUserIsAdmin())) {
            throw new NoPermissionException();
        }
        var event = AuditionEvent.builder()
                .user(userService.getCurrentUser())
                .eventType(AuditionEventType.SUBMISSION_HISTORY_ACQUIRE)
                .message(String.format(
                        "Submission history acquired for user id '%s'",
                        userId))
                .build();
        auditionEventService.addEvent(event);
        var user = userService.getUserById(userId);
        var submissionModels = submissionRepository.getByUserId(userId);
        var collection = new HashSet<Submission>();
        for (var submissionModel : submissionModels) {
            collection.add(SubmissionMapper.MAPPER.toSubmission(submissionModel, user));
        }
        return SubmissionMapper.MAPPER.toSubmissionDTOs(collection);
    }

    /**
     * Retrieves the last submission for a given user based on user ID.
     * Audits the action and logs an audition event.
     *
     * @param userId The ID of the user to retrieve the last submission for.
     * @return Optional<Submission> representing the last submission of the user.
     * @throws NoPermissionException If the current user does not have permission to access the submission.
     */
    private Optional<Submission> findLastSubmissionByUserId(Long userId) {
        if (!(checkUserIsCurrentUser(userId) || checkCurrentUserIsAdmin())) {
            throw new NoPermissionException();
        }
        var event = AuditionEvent.builder()
                .user(userService.getCurrentUser())
                .eventType(AuditionEventType.SINGLE_SUBMISSION_ACQUIRE)
                .message(String.format(
                        "Last submission acquired for user id '%s'",
                        userId))
                .build();
        auditionEventService.addEvent(event);
        var submissionModelOptional
                = submissionRepository.findLastSubmissionByUserId(userId);
        if (submissionModelOptional.isPresent()) {
            var user = userService.getUserById(userId);
            return Optional.of(SubmissionMapper.MAPPER.toSubmission(submissionModelOptional.get(), user));
        }
        return Optional.empty();
    }

    private boolean checkUserIsCurrentUser(Long userId) {
        return userService.getCurrentUser().getId().equals(userId);
    }

    private boolean checkCurrentUserIsAdmin() {
        return userService.getCurrentUser().getRole().equals(UserRole.ADMIN);
    }

    /**
     * Retrieves the last submission for a given user based on user ID and converts it to a DTO.
     * Audits the action and logs an audition event.
     *
     * @param userId The ID of the user to retrieve the last submission for.
     * @return SubmissionDTO representing the last submission of the user.
     * @throws NoPermissionException If the current user does not have permission to access the submission.
     * @throws NoSubmissionException If no submissions are found for the user.
     */
    public SubmissionDTO getLastSubmissionByUserId(Long userId) {
        var user = userService.getUserById(userId);
        var submission = findLastSubmissionByUserId(user.getId())
                .orElseThrow(() -> new NoSubmissionException(user.getName()));
        return SubmissionMapper.MAPPER.toSubmissionDTO(submission);
    }

    public Submission getSubmissionById(Long submissionId) {
        var submissionModel = submissionRepository.getById(submissionId)
                .orElseThrow(()-> new NoSubmissionException(submissionId));
        var user = userService.getUserById(submissionModel.userId());
        return SubmissionMapper.MAPPER.toSubmission(submissionModel, user);
    }

    /**
     * Retrieves a submission for a given user and date.
     * Audits the action and logs an audition event.
     *
     * @param request The request containing user ID and submission date.
     * @return SubmissionDTO representing the submission for the user and date.
     * @throws NoPermissionException If the current user does not have permission to access the submission.
     * @throws NoSubmissionException If no submissions are found for the user and date.
     */
    public SubmissionDTO getSubmissionByDateAndUserId(SubmissionByDateRequestDTO request) {
        var user = userService.getUserById(request.userId());
        var submission = findByUserIdAndDate(user.getId(), request.date())
                .orElseThrow(() -> new NoSubmissionException(user.getName()));
        return SubmissionMapper.MAPPER.toSubmissionDTO(submission);
    }

    private Optional<Submission> findByUserIdAndDate(Long userId, LocalDate date) {
        if (!(checkUserIsCurrentUser(userId) || checkCurrentUserIsAdmin())) {
            throw new NoPermissionException();
        }
        var event = AuditionEvent.builder()
                .user(userService.getCurrentUser())
                .eventType(AuditionEventType.SINGLE_SUBMISSION_ACQUIRE)
                .message(String.format(
                        "Submission acquired for user id '%s' and date '%s-%s'",
                        userId,
                        date.getYear(),
                        date.getMonthValue()))
                .build();
        auditionEventService.addEvent(event);
        var submissionModelOptional = submissionRepository.findSubmissionByUserIdAndDate(userId, date);
        if (submissionModelOptional.isPresent()) {
            var user = userService.getUserById(userId);
            return Optional.of(SubmissionMapper.MAPPER.toSubmission(submissionModelOptional.get(), user));
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
