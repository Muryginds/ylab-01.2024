package ru.ylab.repository;

import ru.ylab.entity.MeterReadings;

import java.time.LocalDate;
import java.util.*;

public class InMemoryMeterReadingsRepository implements MeterReadingsRepository {
    private static final Map<String, TreeMap<LocalDate, MeterReadings>> READING = init();

    private static Map<String, TreeMap<LocalDate, MeterReadings>> init() {
        return new HashMap<>();
    }

    @Override
    public TreeMap<LocalDate, MeterReadings> getUserMeterReadings(String username) {
        return READING.getOrDefault(username, new TreeMap<>());
    }

    @Override
    public void addUserMeterReadings(String username, LocalDate date, MeterReadings meterReadings) {
        var readings = getUserMeterReadings(username);
        readings.put(date, meterReadings);
        READING.put(username, readings);
    }

    @Override
    public boolean checkMeterReadingsExistByDate(String username, LocalDate date) {
        var readings = getUserMeterReadings(username);
        return readings.containsKey(date);
    }

    @Override
    public Map.Entry<LocalDate, MeterReadings> getLastUserMeterReadings(String username) {
        var readings = getUserMeterReadings(username);
        return readings.lastEntry();
    }

    @Override
    public Map<String, TreeMap<LocalDate, MeterReadings>> getAll() {
        return READING;
    }
}
