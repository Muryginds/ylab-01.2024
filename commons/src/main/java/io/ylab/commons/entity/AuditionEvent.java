package io.ylab.commons.entity;

import io.ylab.commons.enumerated.AuditionEventType;
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
