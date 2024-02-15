package io.ylab.dto.response;

import lombok.Builder;

import java.util.Set;

/**
 * Data Transfer Object (DTO) representing a submission.
 *
 * <p>This class is used to transfer submission-related information between different layers of the application,
 * such as between service and controller layers.
 */
@Builder
public record SubmissionDto(
        Long id,
        UserDto userDTO,
        String date,
        Set<MeterReadingDto> readings
) {
}
