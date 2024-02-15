package ru.ylab.dto.response;

import lombok.Builder;
import ru.ylab.enumerated.AuditionEventType;

/**
 * Data Transfer Object (DTO) representing an audition event.
 *
 * <p>This class is used to transfer audition event-related information between different layers of the application,
 * such as between service and controller layers.
 */
@Builder
public record AuditionEventDTO(
        Long id,
        UserDTO userDTO,
        AuditionEventType eventType,
        String message,
        String date
) {
}
