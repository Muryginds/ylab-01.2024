package io.ylab.backend.model;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record SubmissionModel(
        Long id,
        Long userId,
        LocalDate date) {
    public static class SubmissionModelBuilder {
        private LocalDate date = LocalDate.now();
    }
}
