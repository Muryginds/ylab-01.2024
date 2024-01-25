package ru.ylab.entity;

import lombok.Builder;
import lombok.Data;
import ru.ylab.enumerated.AuditionEventType;

import java.time.LocalDateTime;

@Builder
@Data
public class AuditionEvent {
    private static Long userCounter = 1L;

    @Builder.Default
    private final Long id = userCounter++;
    private final User user;
    private final AuditionEventType type;
    private final String message;
    @Builder.Default
    private final LocalDateTime date = LocalDateTime.now();
}
