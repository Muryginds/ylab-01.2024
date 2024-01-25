package ru.ylab.repository;

import ru.ylab.entity.MeterReadings;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

public interface MeterReadingsRepository {
    Collection<MeterReadings> getUserMeterReadings(Long userId);

    void addUserMeterReadings(Long userId, MeterReadings meterReadings);

    boolean checkMeterReadingsExistByDate(Long userId, LocalDate date);

    Optional<MeterReadings> getLastUserMeterReadings(Long userId);

    Collection<MeterReadings> getAll();
}
