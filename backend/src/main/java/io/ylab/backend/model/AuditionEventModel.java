package io.ylab.backend.model;

import io.ylab.commons.enumerated.AuditionEventType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AuditionEventModel (
        Long id,
        Long userId,
        AuditionEventType eventType,
        String message,
        LocalDateTime date
) {
    public static class SubmissionModelBuilder {
        private LocalDateTime date = LocalDateTime.now();
    }
}
