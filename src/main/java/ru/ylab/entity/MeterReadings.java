package ru.ylab.entity;

import ru.ylab.enumerated.MeterType;

import java.util.HashMap;
import java.util.Map;

public record MeterReadings(
        Map<MeterType, Integer> readings
) {
    public MeterReadings {
        if (readings == null) {
            readings = new HashMap<>();
        }
    }
}
