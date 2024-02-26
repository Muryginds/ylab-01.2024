package io.ylab.service;

import io.ylab.dto.response.MeterReadingDto;
import io.ylab.entity.MeterReading;
import io.ylab.entity.Submission;
import io.ylab.mapper.MeterReadingMapper;
import io.ylab.model.MeterReadingModel;
import io.ylab.repository.MeterReadingRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        Long submissionId = 1L;
        Submission submission = Submission.builder().build();
        submission.setId(submissionId);
        Set<MeterReadingModel> meterReadingModels = new HashSet<>();
        MeterReadingDto meterReadingDto = MeterReadingDto.builder().build();
        meterReadingModels.add(MeterReadingModel.builder().build());
        when(submissionService.getSubmissionById(submissionId)).thenReturn(submission);
        when(meterReadingRepository.getAllBySubmissionId(submissionId)).thenReturn(meterReadingModels);
        when(meterReadingMapper.toMeterReadingDTOSet(anySet())).thenReturn(Collections.singleton(meterReadingDto));

        Set<MeterReadingDto> result = meterReadingsService.getAllBySubmissionId(submissionId);

        assertEquals(1, result.size());
        assertEquals(meterReadingDto, result.iterator().next());
    }

    @Test
    void testGetBySubmission() {
        Submission submission = Submission.builder().build();
        submission.setId(1L);
        Set<MeterReadingModel> meterReadings = new HashSet<>();
        meterReadings.add(MeterReadingModel.builder().build());
        when(meterReadingRepository.getAllBySubmissionId(submission.getId())).thenReturn(meterReadings);
        when(meterReadingMapper.toMeterReading(any(), any(), any())).thenReturn(MeterReading.builder().build());

        Set<MeterReading> result = meterReadingsService.getBySubmission(submission);

        assertEquals(1, result.size());
    }

    @Test
    void testSave() {
        MeterReading meterReading = MeterReading.builder().build();

        meterReadingsService.save(meterReading);

        verify(meterReadingRepository, times(1)).save(meterReading);
    }

    @Test
    void testSaveAll() {
        Collection<MeterReading> meterReadings = Collections.singletonList(MeterReading.builder().build());

        meterReadingsService.saveAll(meterReadings);

        verify(meterReadingRepository, times(1)).saveAll(meterReadings);
    }
}
