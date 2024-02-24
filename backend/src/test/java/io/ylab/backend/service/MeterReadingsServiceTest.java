package io.ylab.backend.service;

import io.ylab.backend.dto.response.MeterReadingDto;
import io.ylab.commons.entity.MeterReading;
import io.ylab.commons.entity.Submission;
import io.ylab.backend.mapper.MeterReadingMapper;
import io.ylab.backend.model.MeterReadingModel;
import io.ylab.backend.repository.MeterReadingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeterReadingsServiceTest {
    @Mock
    private MeterReadingRepository meterReadingRepository;
    @Mock
    private SubmissionService submissionService;
    @Mock
    private MeterService meterService;
    @Mock
    private MeterReadingMapper meterReadingMapper;
    @InjectMocks
    private MeterReadingsService meterReadingsService;

    @Test
    void testGetAllBySubmissionId() {
        var submissionId = 1L;
        var submission = Submission.builder().id(submissionId).build();
        var meterReadingModels = new HashSet<MeterReadingModel>();
        var meterReadingDto = MeterReadingDto.builder().build();
        meterReadingModels.add(MeterReadingModel.builder().build());
        when(submissionService.getSubmissionById(submissionId)).thenReturn(submission);
        when(meterReadingRepository.getAllBySubmissionId(submissionId)).thenReturn(meterReadingModels);
        when(meterReadingMapper.toMeterReadingDTOSet(anySet())).thenReturn(Collections.singleton(meterReadingDto));

        var result = meterReadingsService.getAllBySubmissionId(submissionId);

        assertEquals(1, result.size());
        assertEquals(meterReadingDto, result.iterator().next());
    }

    @Test
    void testGetBySubmission() {
        var submission = Submission.builder().id(1L).build();
        var meterReadings = new HashSet<MeterReadingModel>();
        meterReadings.add(MeterReadingModel.builder().build());
        when(meterReadingRepository.getAllBySubmissionId(submission.getId())).thenReturn(meterReadings);
        when(meterReadingMapper.toMeterReading(any(), any(), any())).thenReturn(MeterReading.builder().build());

        var result = meterReadingsService.getBySubmission(submission);

        assertEquals(1, result.size());
    }

    @Test
    void testSave() {
        var meterReading = MeterReading.builder().build();

        meterReadingsService.save(meterReading);

        verify(meterReadingRepository, times(1)).save(meterReading);
    }

    @Test
    void testSaveAll() {
        var meterReadings = Collections.singletonList(MeterReading.builder().build());

        meterReadingsService.saveAll(meterReadings);

        verify(meterReadingRepository, times(1)).saveAll(meterReadings);
    }
}
