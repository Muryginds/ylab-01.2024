package ru.ylab.dto;

import lombok.Builder;
import ru.ylab.enumerated.AuditionEventType;

import java.time.LocalDateTime;

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
        AuditionEventType type,
        String message,
        LocalDateTime date
) {
}
