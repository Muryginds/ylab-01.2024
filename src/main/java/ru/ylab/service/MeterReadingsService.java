package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.MeterReading;
import ru.ylab.dto.MeterReadingDTO;
import ru.ylab.mapper.MeterReadingMapper;
import ru.ylab.repository.MeterReadingsRepository;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * The MeterReadingsService class provides functionality related to meter readings.
 * It includes methods for retrieving meter readings, saving individual readings, and saving a collection of readings.
 * This service interacts with the MeterReadingsRepository.
 */
@RequiredArgsConstructor
public class MeterReadingsService {
    private final MeterReadingsRepository meterReadingsRepository;
    private final SubmissionService submissionService;
    private final MeterService meterService;

    /**
     * Retrieves all meter readings associated with a submission ID.
     *
     * @param submissionId The ID of the submission for which readings are retrieved.
     * @return Set of MeterReadingDTO representing all readings associated with the submission.
     */
    public Set<MeterReadingDTO> getAllBySubmissionId(Long submissionId) {
        var submission = submissionService.getSubmissionById(submissionId);
        var meterReadingModels = meterReadingsRepository.getAllBySubmissionId(submissionId);
        var collection = new HashSet<MeterReading>();
        for (var meterReadingModel : meterReadingModels) {
            var meter = meterService.getById(meterReadingModel.meterId());
            collection.add(MeterReadingMapper.MAPPER.toMeterReading(meterReadingModel, meter, submission));
        }

        return  MeterReadingMapper.MAPPER.toMeterReadingDTOSet(collection);
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
