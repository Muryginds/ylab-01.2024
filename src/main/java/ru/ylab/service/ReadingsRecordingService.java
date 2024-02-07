package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.dto.request.SubmissionRequestDTO;
import ru.ylab.entity.*;
import ru.ylab.enumerated.AuditionEventType;
import ru.ylab.exception.MeterNotFoundException;
import ru.ylab.exception.SubmissionExistsException;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for recording new submissions with meter readings.
 */
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
        var submission = Submission.builder()
                .date(date)
                .user(user)
                .build();
        submissionService.save(submission);
        var readings = request.meterReadings().entrySet().stream()
                .map(newMeterReadingEntry -> {
                    var meter = getUserMeterById(newMeterReadingEntry.getKey(), currentUserMetersMap);
                    return createMeterReading(meter, newMeterReadingEntry.getValue(), submission);
                })
                .collect(Collectors.toSet());
        meterReadingsService.saveAll(readings);

        saveNewSubmissionRecordsAuditionEvent(user, date);
    }

    private void saveNewSubmissionRecordsAuditionEvent(User user, LocalDate date) {
        var event = AuditionEvent.builder()
                .user(user)
                .eventType(AuditionEventType.READINGS_SUBMISSION)
                .message(String.format(
                        "New submission by user id '%s' and date '%s-%s'",
                        user.getId(),
                        date.getYear(),
                        date.getMonthValue()))
                .build();
        auditionEventService.save(event);
    }

    private MeterReading createMeterReading(Meter meter, Long value, Submission submission) {
        return MeterReading.builder()
                .meter(meter)
                .value(value)
                .submission(submission)
                .build();
    }

    private Meter getUserMeterById(Long meterId, Map<Long, Meter> currentUserMetersMap) {
        var meter = currentUserMetersMap.get(meterId);
        if (meter == null) {
            throw new MeterNotFoundException(meterId);
        }
        return meter;
    }
}
