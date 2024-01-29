package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.MeterReading;
import ru.ylab.entity.User;
import ru.ylab.dto.MeterReadingDTO;
import ru.ylab.mapper.MeterReadingMapper;
import ru.ylab.repository.MeterReadingsRepository;

import java.util.Collection;
import java.util.Set;

/**
 * The MeterReadingsService class provides functionality related to meter readings.
 * It includes methods for retrieving meter readings, saving individual readings, and saving a collection of readings.
 * This service interacts with the MeterReadingsRepository.
 */
@RequiredArgsConstructor
public class MeterReadingsService {
    private final MeterReadingsRepository meterReadingsRepository;

    /**
     * Retrieves meter readings associated with a specific user.
     *
     * @param user The user for whom meter readings are retrieved.
     * @return Collection of MeterReading representing the user's meter readings.
     */
    public Collection<MeterReading> getByUser(User user) {
        return meterReadingsRepository.getByUserId(user.getId());
    }

    /**
     * Retrieves all meter readings associated with a submission ID.
     *
     * @param submissionId The ID of the submission for which readings are retrieved.
     * @return Set of MeterReadingDTO representing all readings associated with the submission.
     */
    public Set<MeterReadingDTO> getAllBySubmissionId(Long submissionId) {
        var meterReadings = meterReadingsRepository.getAllBySubmissionId(submissionId);
        return MeterReadingMapper.MAPPER.toMeterReadingDTOSet(meterReadings);
    }

    /**
     * Saves a single meter reading.
     *
     * @param meterReading The meter reading to be saved.
     */
    public void save(MeterReading meterReading) {
        meterReadingsRepository.save(meterReading);
    }

    /**
     * Saves a collection of meter readings.
     *
     * @param meterReadings The collection of meter readings to be saved.
     */
    public void saveAll(Collection<MeterReading> meterReadings) {
        meterReadingsRepository.saveAll(meterReadings);
    }
}
