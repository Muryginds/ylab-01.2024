package ru.ylab.repository.impl;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.MeterReading;
import ru.ylab.repository.MeterReadingsRepository;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class InMemoryMeterReadingsRepository implements MeterReadingsRepository {
    private static final Map<Long, Set<MeterReading>> READINGS = new HashMap<>();

    @Override
    public Set<MeterReading> getByUserId(Long userId) {
        return READINGS.values().stream()
                .flatMap(Collection::stream)
                .filter(mr -> mr.getSubmission().getUser().getId().equals(userId))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<MeterReading> getAllBySubmissionId(Long submissionId) {
        return READINGS.get(submissionId);
    }

    @Override
    public void save(MeterReading meterReading) {
        var submission = meterReading.getSubmission();
        var readings = READINGS.getOrDefault(submission.getId(), new HashSet<>());
        readings.add(meterReading);
        READINGS.put(submission.getId(), readings);
    }

    @Override
    public void saveAll(Collection<MeterReading> meterReadings) {
        for (MeterReading readings: meterReadings) {
            save(readings);
        }
    }
}
