package ru.ylab.repository;

import ru.ylab.entity.MeterReadings;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public interface MeterReadingsRepository {
    TreeMap<LocalDate, MeterReadings> getUserMeterReadings(String username);

    void addUserMeterReadings(String username, LocalDate date, MeterReadings meterReadings);

    boolean checkMeterReadingsExistByDate(String username, LocalDate date);

    Map.Entry<LocalDate, MeterReadings> getLastUserMeterReadings(String username);

    Map<String, TreeMap<LocalDate, MeterReadings>> getAll();
}
