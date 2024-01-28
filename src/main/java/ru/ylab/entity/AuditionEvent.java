package ru.ylab.entity;

import lombok.Builder;
import lombok.Data;
import ru.ylab.enumerated.AuditionEventType;

import java.time.LocalDateTime;

@Builder
@Data
public final class AuditionEvent {
    private static Long counter = 1L;

    @Builder.Default
    private final Long id = counter++;
    private final User user;
    private final AuditionEventType type;
    private final String message;
    @Builder.Default
    private final LocalDateTime date = LocalDateTime.now();
}
