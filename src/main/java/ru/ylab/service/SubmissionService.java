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
import ru.ylab.exception.NoSubmissionsException;
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

@RequiredArgsConstructor
public class SubmissionService {
    private final SubmissionRepository submissionRepository;
    private final MeterReadingsService meterReadingsService;
    private final MeterService meterService;
    private final UserService userService;
    private final AuditionEventService auditionEventService;

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

    public SubmissionDTO getLastSubmissionByUserId(Long userId) {
        var user = userService.getUserById(userId);
        var submission = findLastSubmissionByUserId(user.getId())
                .orElseThrow(() -> new NoSubmissionsException(user.getName()));
        return SubmissionMapper.MAPPER.toSubmissionDTO(submission);
    }

    public SubmissionDTO getSubmissionByDateAndUserId(SubmissionByDateRequestDTO request) {
        var user = userService.getUserById(request.userId());
        var submission = findByUserIdAndDate(user.getId(), request.date())
                .orElseThrow(() -> new NoSubmissionsException(user.getName()));
        return SubmissionMapper.MAPPER.toSubmissionDTO(submission);
    }

    public Optional<Submission> findByUserIdAndDate(Long userId, LocalDate date) {
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
