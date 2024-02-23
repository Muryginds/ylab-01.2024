package io.ylab.backend.entity;

import io.ylab.backend.enumerated.AuditionEventType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public final class AuditionEvent {
    private Long id;
    private User user;
    private AuditionEventType eventType;
    private String message;
    @Builder.Default
    private LocalDateTime date = LocalDateTime.now();
}
