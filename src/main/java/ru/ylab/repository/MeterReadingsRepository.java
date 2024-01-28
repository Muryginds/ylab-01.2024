package ru.ylab.repository;

import ru.ylab.entity.MeterReading;

import java.util.Collection;
import java.util.Set;

/**
 * Repository interface for managing meter reading-related data operations.
 */
public interface MeterReadingsRepository {

    /**
     * Gets all meter readings associated with a specific user.
     *
     * @param userId The ID of the user.
     * @return A set of meter readings associated with the specified user.
     */
    Set<MeterReading> getByUserId(Long userId);

    /**
     * Gets all meter readings associated with a specific submission.
     *
     * @param submissionId The ID of the submission.
     * @return A set of meter readings associated with the specified submission.
     */
    Set<MeterReading> getAllBySubmissionId(Long submissionId);

    /**
     * Saves a single meter reading in the repository.
     *
     * @param meterReading The meter reading to be saved.
     */
    void save(MeterReading meterReading);

    /**
     * Saves a collection of meter readings in the repository.
     *
     * @param meterReadings The collection of meter readings to be saved.
     */
    void saveAll(Collection<MeterReading> meterReadings);
}
