package ru.ylab.in.dto;

import lombok.Builder;
import ru.ylab.enumerated.AuditionEventType;

import java.time.LocalDateTime;

@Builder
public record AuditionEventDTO(
        Long id,
        UserDTO userDTO,
        AuditionEventType type,
        String message,
        LocalDateTime date
) {
}
