package ru.ylab.in.dto;

import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing a meter reading.
 *
 * <p>This class is used to transfer meter reading-related information between different layers of the application,
 * such as between service and controller layers.
 */
@Builder
public record MeterReadingDTO(
        Long id,
        MeterDTO meterDTO,
        SubmissionDTO submissionDTO,
        Long value
) {
}
