package ru.ylab.entity;

import lombok.Builder;
import lombok.Data;
import ru.ylab.enumerated.MeterType;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

@Builder
@Data
public class MeterReadings {
    private static Long userCounter = 0L;

    @Builder.Default
    private final Long id = userCounter++;
    private final User user;
    @Builder.Default
    private final LocalDate date = LocalDate.now();
    @Builder.Default
    private final Map<MeterType, Integer> readings = new EnumMap<>(MeterType.class);
}
