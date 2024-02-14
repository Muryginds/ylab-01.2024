package io.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import io.ylab.entity.Meter;
import io.ylab.entity.MeterReading;
import io.ylab.entity.MeterType;
import io.ylab.entity.Submission;
import io.ylab.mapper.MeterReadingMapper;
import io.ylab.repository.MeterReadingRepository;

import java.util.Set;

class MeterReadingsServiceTest {

    @Mock
    private MeterReadingRepository meterReadingRepository;

    @Mock
    private SubmissionService submissionService;

    @Mock
    private MeterService meterService;

    @InjectMocks
    private MeterReadingsService meterReadingsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBySubmissionId_whenExistingSubmissionId_thenReturnSetOfMeterReadingDTOs() {
        var submissionId = 1L;
        var submission = Submission.builder().id(submissionId).build();
        var typeElectricity = MeterType.builder().typeName("Electricity").build();
        var typeGas = MeterType.builder().typeName("Gas").build();
        var meter1 = Meter.builder().id(1L).factoryNumber("123456789012").meterType(typeElectricity).build();
        var meter2 = Meter.builder().id(2L).factoryNumber("987654321098").meterType(typeGas).build();
        var meterReading1 = MeterReading.builder().id(1L).submission(submission).meter(meter1).value(100L).build();
        var meterReading2 = MeterReading.builder().id(2L).submission(submission).meter(meter2).value(150L).build();

        var meterReadings = Set.of(meterReading1, meterReading2);
        var meterReadingsModels = Set.of(
                MeterReadingMapper.MAPPER.toMeterReadingModel(meterReading1),
                MeterReadingMapper.MAPPER.toMeterReadingModel(meterReading2)
        );
        Mockito.when(meterReadingRepository.getAllBySubmissionId(submissionId)).thenReturn(meterReadingsModels);
        Mockito.when(submissionService.getSubmissionById(submissionId)).thenReturn(submission);
        Mockito.when(meterService.getById(1L)).thenReturn(meter1);
        Mockito.when(meterService.getById(2L)).thenReturn(meter2);

        var result = meterReadingsService.getAllBySubmissionId(submissionId);

        var expected = MeterReadingMapper.MAPPER.toMeterReadingDTOSet(meterReadings);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testSaveMeterReading_whenValid_thenDoNothing() {
        var meterReading = MeterReading.builder()
                .id(1L)
                .submission(Submission.builder().build())
                .meter(Meter.builder().build())
                .value(100L)
                .build();

        meterReadingsService.save(meterReading);

        Mockito.verify(meterReadingRepository, Mockito.times(1)).save(meterReading);
    }

    @Test
    void testSaveAllMeterReadings_whenValid_thenDoNothing() {
        var submission = Submission.builder().build();
        var meterReadings = Set.of(
                MeterReading.builder().id(1L).submission(submission).value(100L).build(),
                MeterReading.builder().id(2L).submission(submission).value(150L).build()
        );

        meterReadingsService.saveAll(meterReadings);

        Mockito.verify(meterReadingRepository, Mockito.times(1)).saveAll(meterReadings);
    }
}
