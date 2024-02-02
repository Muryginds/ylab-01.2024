package ru.ylab.model;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record AuditionEventModel (
        Long id,
        Long userId,
        Long typeId,
        String message,
        LocalDateTime date
) {
    public static class SubmissionModelBuilder {
        private final LocalDateTime date = LocalDateTime.now();
    }
}
