package io.ylab.service;

import io.ylab.annotation.Loggable;
import io.ylab.dto.response.MessageDto;
import io.ylab.utils.ResponseUtils;
import io.ylab.utils.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import io.ylab.annotation.Auditable;
import io.ylab.dto.request.NewReadingsSubmissionRequestDto;
import io.ylab.entity.Meter;
import io.ylab.entity.MeterReading;
import io.ylab.entity.Submission;
import io.ylab.enumerated.AuditionEventType;
import io.ylab.exception.MeterNotFoundException;
import io.ylab.exception.SubmissionExistsException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service responsible for recording new submissions with meter readings.
 */
@Service
@RequiredArgsConstructor
public class ReadingsRecordingService {
    private final MeterReadingsService meterReadingsService;
    private final MeterService meterService;
    private final SubmissionService submissionService;

    /**
     * Saves a new submission with the provided meter readings.
     *
     * @param requestDto The request containing user ID and meter readings.
     * @throws SubmissionExistsException If a submission already exists for the user on the current date.
     * @throws MeterNotFoundException If a meter specified in the readings is not found.
     */
    @Loggable
    @Auditable(eventType = AuditionEventType.READINGS_SUBMISSION)
    public MessageDto saveNewSubmission(NewReadingsSubmissionRequestDto requestDto) {
        var date = LocalDate.now();
        var user = CurrentUserUtils.getCurrentUser();
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
        var readings = requestDto.meterReadings().stream()
                .map(newReadingRequest -> {
                    var meter = getUserMeterById(newReadingRequest.meterId(), currentUserMetersMap);
                    return createMeterReading(meter, newReadingRequest.value(), submission);
                })
                .collect(Collectors.toSet());
        meterReadingsService.saveAll(readings);
        return ResponseUtils.responseWithMessage("New submission saved");
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
