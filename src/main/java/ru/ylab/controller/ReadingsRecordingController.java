package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.dto.request.SubmissionRequestDTO;
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
    public void saveNewSubmission(SubmissionRequestDTO request) {
        readingsRecordingService.saveNewSubmission(request);
    }
}
