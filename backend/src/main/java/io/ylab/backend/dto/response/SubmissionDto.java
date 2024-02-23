package io.ylab.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.util.Set;

/**
 * Data Transfer Object (DTO) representing a submission.
 *
 * <p>This class is used to transfer submission-related information between different layers of the application,
 * such as between service and controller layers.
 */
@Builder
@Schema(description = "Модель данных показаний")
public record SubmissionDto(
        Long id,
        UserDto userDTO,
        String date,
        Set<MeterReadingDto> readings
) {
}
