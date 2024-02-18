package io.ylab.repository;

import io.ylab.entity.MeterReading;
import io.ylab.model.MeterReadingModel;

import java.util.Collection;

/**
 * Repository interface for managing meter reading-related data operations.
 */
public interface MeterReadingRepository {

    /**
     * Gets all meter readings associated with a specific submission.
     *
     * @param submissionId The ID of the submission.
     * @return A set of meter reading model associated with the specified submission.
     */
    Collection<MeterReadingModel> getAllBySubmissionId(Long submissionId);

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
