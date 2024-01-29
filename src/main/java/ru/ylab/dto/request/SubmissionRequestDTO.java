package ru.ylab.dto.request;

import lombok.Builder;

import java.util.Map;

/**
 * Data Transfer Object (DTO) representing a submission request.
 *
 * <p>This class is used to transfer information about a user's meter readings submission from the client to the server.
 */
@Builder
public record SubmissionRequestDTO(
        Map<Long, Long> meterReadings
) {
}
