package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.dto.request.SubmissionRequestDTO;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.entity.Meter;
import ru.ylab.entity.MeterReading;
import ru.ylab.entity.Submission;
import ru.ylab.enumerated.AuditionEventType;
import ru.ylab.exception.MeterNotFoundException;
import ru.ylab.exception.SubmissionExistsException;

import java.time.LocalDate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class ReadingsRecordingService {
    private final MeterReadingsService meterReadingsService;
    private final MeterService meterService;
    private final SubmissionService submissionService;
    private final UserService userService;
    private final AuditionEventService auditionEventService;

    /**
     * Saves a new submission with the provided meter readings.
     * Audits the action, logs an audition event, and performs necessary validations.
     *
     * @param request The request containing user ID and meter readings.
     * @throws SubmissionExistsException If a submission already exists for the user on the current date.
     * @throws MeterNotFoundException If a meter specified in the readings is not found.
     */
    public void saveNewSubmission(SubmissionRequestDTO request) {
        var date = LocalDate.now();
        var user = userService.getCurrentUser();
        if (submissionService.checkExistsByUserIdAndDate(user.getId(), date)) {
            throw new SubmissionExistsException(user.getName(), date);
        }
        var currentUserMetersMap = meterService.getMetersByUserId(user.getId()).stream()
                .collect(Collectors.toMap(Meter::getId, m -> m));
        var submission = Submission.builder().date(date).user(user).build();
        submissionService.save(submission);
        var readings = request.meterReadings().entrySet().stream()
                .map(entry -> MeterReading.builder()
                        .meter(checkMeterIsFound(currentUserMetersMap.get(entry.getKey()), entry.getKey()))
                        .value(entry.getValue())
                        .submission(submission)
                        .build()
                )
                .collect(Collectors.toSet());
        meterReadingsService.saveAll(readings);
        var event = AuditionEvent.builder()
                .user(user)
                .eventType(AuditionEventType.READINGS_SUBMISSION)
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
