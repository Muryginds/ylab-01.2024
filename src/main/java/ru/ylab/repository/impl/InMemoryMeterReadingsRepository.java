package ru.ylab.repository;

import ru.ylab.entity.MeterReadings;

import java.time.LocalDate;
import java.util.*;

public class InMemoryMeterReadingsRepository implements MeterReadingsRepository {
    private static final Map<Long, TreeMap<LocalDate, MeterReadings>> READING = init();

    private static Map<Long, TreeMap<LocalDate, MeterReadings>> init() {
        return new HashMap<>();
    }

    private static TreeMap<LocalDate, MeterReadings> getUserMeterReadingsMap(Long userId) {
        return READING.getOrDefault(userId, new TreeMap<>());
    }

    @Override
    public Collection<MeterReadings> getUserMeterReadings(Long userId) {
        return getUserMeterReadingsMap(userId).values();
    }

    @Override
    public void addUserMeterReadings(Long userId, MeterReadings meterReadings) {
        var readings = getUserMeterReadingsMap(userId);
        readings.put(meterReadings.getDate(), meterReadings);
        READING.put(userId, readings);
    }

    @Override
    public boolean checkMeterReadingsExistByDate(Long userId, LocalDate date) {
        var readings = getUserMeterReadingsMap(userId);
        return readings.containsKey(date);
    }

    @Override
    public Optional<MeterReadings> getLastUserMeterReadings(Long userId) {
        var readings = getUserMeterReadingsMap(userId);
        return Optional.ofNullable(readings.lastEntry().getValue());
    }

    @Override
    public Collection<MeterReadings> getAll() {
        return READING.values().stream().flatMap(t -> t.values().stream()).toList();
    }
}
