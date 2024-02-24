package io.ylab.backend.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import io.ylab.commons.enumerated.AuditionEventType;
import lombok.Builder;

/**
 * Data Transfer Object (DTO) representing an audition event.
 *
 * <p>This class is used to transfer audition event-related information between different layers of the application,
 * such as between service and controller layers.
 */
@Builder
@Schema(description = "Модель данных аудита")
public record AuditionEventDto(
        Long id,
        UserDto userDTO,
        AuditionEventType eventType,
        String message,
        String date
) {
}
