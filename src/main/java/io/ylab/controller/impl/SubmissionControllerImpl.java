package io.ylab.controller.impl;

import io.ylab.controller.SubmissionController;
import io.ylab.dto.request.NewReadingsSubmissionRequestDto;
import io.ylab.dto.response.MessageDto;
import io.ylab.dto.response.SubmissionDto;
import io.ylab.service.ReadingsRecordingService;
import io.ylab.service.SubmissionRepresentationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Set;

/**
 * Implementation of controller class for handling submission-related operations and interactions.
 *
 * <p>This class acts as an interface between the user interface and the underlying business logic
 * related to user submissions.
 */
@RestController
@RequestMapping("/api/v1/submissions")
@RequiredArgsConstructor
public class SubmissionControllerImpl implements SubmissionController {
    private final SubmissionRepresentationService submissionRepresentationService;
    private final ReadingsRecordingService readingsRecordingService;

    /**
     * Retrieves all submissions for a user with the specified ID.
     *
     * @param userId containing ID of the user for whom submissions are retrieved.
     * @return A collection of SubmissionDTO representing the user's submissions.
     */
    @Override
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<SubmissionDto> getAllSubmissionDTOs(@RequestParam(name = "userId") long userId) {
        return submissionRepresentationService.getAllSubmissionDTOs(userId);
    }

    /**
     * Retrieves a submission for a user based on the specified date.
     *
     * @param date request containing submission date.
     * @param userId containing the user ID.
     * @return The SubmissionDTO representing the user's submission on the specified date.
     */
    @Override
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public SubmissionDto getSubmissionDTO(@RequestParam(name = "date", required = false) LocalDate date,
                                   @RequestParam(name = "userId", required = false) Long userId) {
        return submissionRepresentationService.getSubmissionDTO(date, userId);
    }

    /**
     * Saves a new submission based on the provided SubmissionRequestDTO.
     * Delegates the task to the ReadingsRecordingService.
     *
     * @param requestDto The SubmissionRequestDTO containing data for the new submission.
     */
    @Override
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageDto saveNewSubmission(@Valid @RequestBody NewReadingsSubmissionRequestDto requestDto) {
        return readingsRecordingService.saveNewSubmission(requestDto);
    }
}
