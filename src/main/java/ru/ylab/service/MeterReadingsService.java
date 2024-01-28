package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.MeterReading;
import ru.ylab.entity.User;
import ru.ylab.in.dto.MeterReadingDTO;
import ru.ylab.mapper.MeterReadingMapper;
import ru.ylab.repository.MeterReadingsRepository;

import java.util.Collection;
import java.util.Set;

@RequiredArgsConstructor
public class MeterReadingsService {
    private final MeterReadingsRepository meterReadingsRepository;

    public Collection<MeterReading> getByUser(User user) {
        return meterReadingsRepository.getByUserId(user.getId());
    }

    public Set<MeterReadingDTO> getAllBySubmissionId(Long submissionId) {
        var meterReadings = meterReadingsRepository.getAllBySubmissionId(submissionId);
        return MeterReadingMapper.MAPPER.toMeterReadingDTOSet(meterReadings);
    }

    public void save(MeterReading meterReading) {
        meterReadingsRepository.save(meterReading);
    }

    public void saveAll(Collection<MeterReading> meterReadings) {
        meterReadingsRepository.saveAll(meterReadings);
    }
}
