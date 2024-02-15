package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.annotation.Auditable;
import ru.ylab.dto.request.NewReadingsSubmissionRequestDTO;
import ru.ylab.entity.Meter;
import ru.ylab.entity.MeterReading;
import ru.ylab.entity.Submission;
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

    /**
     * Saves a new submission with the provided meter readings.
     *
     * @param request The request containing user ID and meter readings.
     * @throws SubmissionExistsException If a submission already exists for the user on the current date.
     * @throws MeterNotFoundException If a meter specified in the readings is not found.
     */
    @Auditable(eventType = AuditionEventType.READINGS_SUBMISSION)
    public void saveNewSubmission(NewReadingsSubmissionRequestDTO request) {
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
        var readings = request.meterReadings().stream()
                .map(newReadingRequest -> {
                    var meter = getUserMeterById(newReadingRequest.meterId(), currentUserMetersMap);
                    return createMeterReading(meter, newReadingRequest.value(), submission);
                })
                .collect(Collectors.toSet());
        meterReadingsService.saveAll(readings);
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
