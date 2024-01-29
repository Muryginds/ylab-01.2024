package ru.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.ylab.entity.Meter;
import ru.ylab.entity.MeterReading;
import ru.ylab.entity.Submission;
import ru.ylab.entity.User;
import ru.ylab.dto.MeterReadingDTO;
import ru.ylab.mapper.MeterReadingMapper;
import ru.ylab.repository.MeterReadingsRepository;

import java.util.Collection;
import java.util.Set;

class MeterReadingsServiceTest {

    @Mock
    private MeterReadingsRepository meterReadingsRepository;

    @InjectMocks
    private MeterReadingsService meterReadingsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByUser_whenExistingUser_thenReturnCollectionOfMeterReadings() {
        long userId = 1L;
        User user = User.builder().id(userId).name("testUser").password("password").build();
        Submission submission = Submission.builder().user(user).build();
        Set<MeterReading> meterReadings = Set.of(
                MeterReading.builder().id(1L).submission(submission).value(100L).build(),
                MeterReading.builder().id(2L).submission(submission).value(150L).build()
        );
        Mockito.when(meterReadingsRepository.getByUserId(userId)).thenReturn(meterReadings);

        Collection<MeterReading> result = meterReadingsService.getByUser(user);

        Assertions.assertEquals(meterReadings, result);
    }

    @Test
    void testGetAllBySubmissionId_whenExistingSubmissionId_thenReturnSetOfMeterReadingDTOs() {
        long submissionId = 1L;
        Submission submission = Submission.builder().build();
        Set<MeterReading> meterReadings = Set.of(
                MeterReading.builder().id(1L).submission(submission).value(100L).build(),
                MeterReading.builder().id(2L).submission(submission).value(150L).build()
        );
        Mockito.when(meterReadingsRepository.getAllBySubmissionId(submissionId)).thenReturn(meterReadings);

        Set<MeterReadingDTO> result = meterReadingsService.getAllBySubmissionId(submissionId);

        Set<MeterReadingDTO> expected = MeterReadingMapper.MAPPER.toMeterReadingDTOSet(meterReadings);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testSaveMeterReading_whenValid_thenDoNothing() {
        MeterReading meterReading = MeterReading.builder()
                .id(1L)
                .submission(Submission.builder().build())
                .meter(Meter.builder().build())
                .value(100L)
                .build();

        meterReadingsService.save(meterReading);

        Mockito.verify(meterReadingsRepository, Mockito.times(1)).save(meterReading);
    }

    @Test
    void testSaveAllMeterReadings_whenValid_thenDoNothing() {
        Submission submission = Submission.builder().build();
        Set<MeterReading> meterReadings = Set.of(
                MeterReading.builder().id(1L).submission(submission).value(100L).build(),
                MeterReading.builder().id(2L).submission(submission).value(150L).build()
        );

        meterReadingsService.saveAll(meterReadings);

        Mockito.verify(meterReadingsRepository, Mockito.times(1)).saveAll(meterReadings);
    }
}
