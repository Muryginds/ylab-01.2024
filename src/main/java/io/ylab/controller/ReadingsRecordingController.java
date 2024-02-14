package io.ylab.controller;

import lombok.RequiredArgsConstructor;
import io.ylab.annotation.Loggable;
import io.ylab.dto.request.NewReadingsSubmissionRequestDTO;
import io.ylab.service.ReadingsRecordingService;

/**
 * Controller class responsible for handling requests related to recording readings.
 * This controller delegates the task of saving new submissions to a service layer.
 */
@RequiredArgsConstructor
public class ReadingsRecordingController {
    private final ReadingsRecordingService readingsRecordingService;

    /**
     * Saves a new submission based on the provided SubmissionRequestDTO.
     * Delegates the task to the ReadingsRecordingService.
     *
     * @param request The SubmissionRequestDTO containing data for the new submission.
     */
    @Loggable
    public void saveNewSubmission(NewReadingsSubmissionRequestDTO request) {
        readingsRecordingService.saveNewSubmission(request);
    }
}
