package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.dto.request.SubmissionRequestDTO;
import ru.ylab.service.ReadingsRecordingService;

@RequiredArgsConstructor
public class ReadingsRecordingController {
    private final ReadingsRecordingService readingsRecordingService;

    /**
     * Saves a new submission based on the provided submission request.
     *
     * @param request The submission request containing meter readings.
     */
    public void saveNewSubmission(SubmissionRequestDTO request) {
        readingsRecordingService.saveNewSubmission(request);
    }
}
