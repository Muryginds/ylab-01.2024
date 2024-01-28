package ru.ylab.repository;

import ru.ylab.entity.MeterReading;

import java.util.Collection;
import java.util.Set;

public interface MeterReadingsRepository {
    Set<MeterReading> getByUserId(Long userId);

    Set<MeterReading> getAllBySubmissionId(Long submissionId);

    void save(MeterReading meterReading);

    void saveAll(Collection<MeterReading> meterReadings);
}
