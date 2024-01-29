package ru.ylab.in.dto.request;

import lombok.Builder;

import java.time.LocalDate;

/**
 * Data Transfer Object (DTO) representing a request to retrieve a submission by date and user ID.
 *
 * <p>This class is used to transfer information about a user's request to retrieve a submission by specifying a date and user ID.
 */
@Builder
public record SubmissionByDateRequestDTO(
            LocalDate date,
            Long userId
) {
}
