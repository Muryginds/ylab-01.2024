package io.ylab.backend.service;

import io.ylab.commons.entity.MeterReading;
import io.ylab.commons.entity.Submission;
import io.ylab.backend.mapper.MeterReadingMapper;
import io.ylab.backend.repository.MeterReadingRepository;
import lombok.RequiredArgsConstructor;
import io.ylab.backend.dto.response.MeterReadingDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The MeterReadingsService class provides functionality related to meter readings.
 * It includes methods for retrieving meter readings, saving individual readings, and saving a collection of readings.
 * This service interacts with the MeterReadingsRepository.
 */
@Service
@RequiredArgsConstructor
public class MeterReadingsService {
    private final MeterReadingRepository meterReadingRepository;
    private final SubmissionService submissionService;
    private final MeterService meterService;
    private final MeterReadingMapper meterReadingMapper;

    /**
     * Retrieves all meter readings associated with a submission ID.
     *
     * @param submissionId The ID of the submission for which readings are retrieved.
     * @return Set of MeterReadingDTO representing all readings associated with the submission.
     */
    @Transactional
    public Set<MeterReadingDto> getAllBySubmissionId(Long submissionId) {
        var submission = submissionService.getSubmissionById(submissionId);
        var meterReadingModels = meterReadingRepository.getAllBySubmissionId(submissionId);
        var collection = new HashSet<MeterReading>();
        for (var meterReadingModel : meterReadingModels) {
            var meter = meterService.getById(meterReadingModel.meterId());
            collection.add(meterReadingMapper.toMeterReading(meterReadingModel, meter, submission));
        }

        return meterReadingMapper.toMeterReadingDTOSet(collection);
    }

    /**
     * Retrieves all meter readings associated with a submission ID.
     *
     * @param submission The submission for which readings are retrieved.
     * @return Set of MeterReadingDTO representing all readings associated with the submission.
     */
    @Transactional
    public Set<MeterReading> getBySubmission(Submission submission) {
        var meterReadingModels = meterReadingRepository.getAllBySubmissionId(submission.getId());
        var collection = new HashSet<MeterReading>();
        for (var meterReadingModel : meterReadingModels) {
            var meter = meterService.getById(meterReadingModel.meterId());
            collection.add(meterReadingMapper.toMeterReading(meterReadingModel, meter, submission));
        }

        return collection;
    }

    /**
     * Saves a single meter reading.
     *
     * @param meterReading The meter reading to be saved.
     */
    public void save(MeterReading meterReading) {
        meterReadingRepository.save(meterReading);
    }

    /**
     * Saves a collection of meter readings.
     *
     * @param meterReadings The collection of meter readings to be saved.
     */
    public void saveAll(Collection<MeterReading> meterReadings) {
        meterReadingRepository.saveAll(meterReadings);
    }
}
