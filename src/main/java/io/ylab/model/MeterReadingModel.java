package io.ylab.model;

import lombok.Builder;

@Builder
public record MeterReadingModel(
        Long id,
        Long submissionId,
        Long meterId,
        Long value) {
    public static class MeterReadingModelBuilder {
        private Long value = 0L;
    }
}
