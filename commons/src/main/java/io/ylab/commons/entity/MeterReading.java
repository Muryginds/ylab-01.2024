package io.ylab.commons.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public final class MeterReading {
    private Long id;
    private Submission submission;
    private Meter meter;
    @Builder.Default
    private Long value = 0L;
}
