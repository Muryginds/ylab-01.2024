package ru.ylab.entity;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public final class Meter {
    private static Long counter = 1L;

    @Builder.Default
    private final Long id = counter++;
    private final String factoryNumber;
    private final User user;
    private final MeterType type;
}
