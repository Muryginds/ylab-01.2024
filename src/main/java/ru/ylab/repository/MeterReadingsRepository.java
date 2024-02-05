package ru.ylab.repository;

import ru.ylab.entity.MeterReading;
import ru.ylab.model.MeterReadingModel;

import java.util.Collection;
import java.util.Set;

/**
 * Repository interface for managing meter reading-related data operations.
 */
public interface MeterReadingsRepository {

    /**
     * Gets all meter readings associated with a specific submission.
     *
     * @param submissionId The ID of the submission.
     * @return A set of meter reading model associated with the specified submission.
     */
    Set<MeterReadingModel> getAllBySubmissionId(Long submissionId);

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
