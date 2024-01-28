package ru.ylab.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public final class MeterReading {
    private static Long counter = 1L;

    @Builder.Default
    private final Long id = counter++;
    private final Submission submission;
    private final Meter meter;
    @Builder.Default
    private final Long value = 0L;
}
