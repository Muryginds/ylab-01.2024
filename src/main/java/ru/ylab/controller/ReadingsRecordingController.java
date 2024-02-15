package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.annotation.Loggable;
import ru.ylab.dto.request.NewReadingsSubmissionRequestDTO;
import ru.ylab.service.ReadingsRecordingService;

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
