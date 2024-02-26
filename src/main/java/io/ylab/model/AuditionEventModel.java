package io.ylab.model;

import lombok.Builder;
import io.ylab.enumerated.AuditionEventType;

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
