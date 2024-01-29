package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.entity.Meter;
import ru.ylab.entity.MeterReading;
import ru.ylab.entity.Submission;
import ru.ylab.enumerated.AuditionEventType;
import ru.ylab.enumerated.UserRole;
import ru.ylab.exception.MeterNotFoundException;
import ru.ylab.exception.NoPermissionException;
import ru.ylab.exception.NoSubmissionException;
import ru.ylab.exception.SubmissionExistsException;
import ru.ylab.in.dto.SubmissionDTO;
import ru.ylab.in.dto.request.SubmissionRequestDTO;
import ru.ylab.in.dto.request.SubmissionByDateRequestDTO;
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
 * This service interacts with the SubmissionRepository, MeterReadingsService, MeterService,
 * UserService, and AuditionEventService to perform various submission-related tasks.
 */
@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final MeterReadingsService meterReadingsService;
    private final MeterService meterService;
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
                .type(AuditionEventType.SUBMISSION_HISTORY_ACQUIRE)
                .message(String.format(
                        "Submission history acquired for user id '%s'",
                        userId))
                .build();
        auditionEventService.addEvent(event);
        var submissions = submissionRepository.getByUserId(userId);
        return SubmissionMapper.MAPPER.toSubmissionDTOs(submissions);
    }

    /**
     * Retrieves the last submission for a given user based on user ID.
     * Audits the action and logs an audition event.
     *
     * @param userId The ID of the user to retrieve the last submission for.
     * @return Optional<Submission> representing the last submission of the user.
     * @throws NoPermissionException If the current user does not have permission to access the submission.
     */
    public Optional<Submission> findLastSubmissionByUserId(Long userId) {
        if (!(checkUserIsCurrentUser(userId) || checkCurrentUserIsAdmin())) {
            throw new NoPermissionException();
        }
        var event = AuditionEvent.builder()
                .user(userService.getCurrentUser())
                .type(AuditionEventType.SINGLE_SUBMISSION_ACQUIRE)
                .message(String.format(
                        "Last submission acquired for user id '%s'",
                        userId))
                .build();
        auditionEventService.addEvent(event);
        return submissionRepository.findLastByUserId(userId);
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
                .type(AuditionEventType.SINGLE_SUBMISSION_ACQUIRE)
                .message(String.format(
                        "Submission acquired for user id '%s' and date '%s-%s'",
                        userId,
                        date.getYear(),
                        date.getMonthValue()))
                .build();
        auditionEventService.addEvent(event);
        return submissionRepository.findByUserIdAndDate(userId, date);
    }

    /**
     * Saves a new submission with the provided meter readings.
     * Audits the action, logs an audition event, and performs necessary validations.
     *
     * @param request The request containing user ID and meter readings.
     * @throws SubmissionExistsException If a submission already exists for the user on the current date.
     * @throws MeterNotFoundException If a meter specified in the readings is not found.
     */
    public void save(SubmissionRequestDTO request) {
        var date = LocalDate.now();
        var user = userService.getCurrentUser();
        if (submissionRepository.checkExistsByUserIdAndDate(user.getId(), date)) {
            throw new SubmissionExistsException(user.getName(), date);
        }
        var currentUserMetersMap = meterService.getMetersByUserId(user.getId()).stream()
                .collect(Collectors.toMap(Meter::getId, m -> m));
        var submission = Submission.builder().date(date).user(user).build();
        var readings = request.meterReadings().entrySet().stream()
                .map(entry -> MeterReading.builder()
                        .meter(checkMeterIsFound(currentUserMetersMap.get(entry.getKey()), entry.getKey()))
                        .value(entry.getValue())
                        .submission(submission)
                        .build()
                )
                .collect(Collectors.toSet());
        submissionRepository.save(submission);
        meterReadingsService.saveAll(readings);
        var event = AuditionEvent.builder()
                .user(user)
                .type(AuditionEventType.READINGS_SUBMISSION)
                .message(String.format(
                        "New submission by user id '%s' and date '%s-%s'",
                        user.getId(),
                        date.getYear(),
                        date.getMonthValue()))
                .build();
        auditionEventService.addEvent(event);
    }

    private Meter checkMeterIsFound(Meter meter, Long meterId) {
        if (meter == null) {
            throw new MeterNotFoundException(meterId);
        }
        return meter;
    }
}
