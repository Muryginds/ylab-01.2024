package io.ylab.entity;

import lombok.Builder;
import lombok.Data;
import io.ylab.enumerated.AuditionEventType;

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
