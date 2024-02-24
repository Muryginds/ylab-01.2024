package io.ylab.backend.service;

import io.ylab.backend.dto.request.NewReadingsSubmissionRequestDto;
import io.ylab.backend.dto.request.ReadingRequestDto;
import io.ylab.backend.dto.response.MessageDto;
import io.ylab.commons.entity.User;
import io.ylab.backend.exception.MeterNotFoundException;
import io.ylab.backend.exception.SubmissionExistsException;
import io.ylab.backend.utils.CurrentUserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReadingsRecordingServiceTest {
    @Mock
    private MeterReadingsService meterReadingsService;
    @Mock
    private MeterService meterService;
    @Mock
    private SubmissionService submissionService;
    @InjectMocks
    private ReadingsRecordingService readingsRecordingService;

    private MockedStatic<CurrentUserUtils> utilsMockedStatic;

    @BeforeEach
    void setUp() {
        utilsMockedStatic = Mockito.mockStatic(CurrentUserUtils.class);
    }

    @AfterEach
    void tearDown() {
        utilsMockedStatic.close();
    }

    @Test
    void testSaveNewSubmission_Success() {
        var requestDto = NewReadingsSubmissionRequestDto.builder().meterReadings(Collections.emptyList()).build();
        var user = User.builder().id(1L).build();
        when(CurrentUserUtils.getCurrentUser()).thenReturn(user);
        when(submissionService.checkExistsByUserIdAndDate(anyLong(), any(LocalDate.class))).thenReturn(false);
        when(meterService.getMetersByUserId(anyLong())).thenReturn(Collections.emptyList());

        MessageDto result = readingsRecordingService.saveNewSubmission(requestDto);

        assertEquals("New submission saved", result.message());
        verify(meterReadingsService, times(1)).saveAll(Collections.emptySet());
    }

    @Test
    void testSaveNewSubmission_SubmissionExistsException() {
        var user = User.builder().id(1L).build();
        when(CurrentUserUtils.getCurrentUser()).thenReturn(user);
        when(submissionService.checkExistsByUserIdAndDate(anyLong(), any(LocalDate.class))).thenReturn(true);
        var submissionRequest = NewReadingsSubmissionRequestDto.builder().build();

        assertThrows(SubmissionExistsException.class,
                () -> readingsRecordingService.saveNewSubmission(submissionRequest));
        verifyNoInteractions(meterReadingsService);
    }

    @Test
    void testSaveNewSubmission_MeterNotFoundException() {
        var requestDto = NewReadingsSubmissionRequestDto.builder()
                .meterReadings(List.of(ReadingRequestDto.builder().meterId(100L).build()))
                .build();
        var user = User.builder().id(1L).build();
        when(CurrentUserUtils.getCurrentUser()).thenReturn(user);
        when(submissionService.checkExistsByUserIdAndDate(anyLong(), any(LocalDate.class))).thenReturn(false);
        when(meterService.getMetersByUserId(anyLong())).thenReturn(Collections.emptyList());

        assertThrows(MeterNotFoundException.class, () -> readingsRecordingService.saveNewSubmission(requestDto));
        verifyNoInteractions(meterReadingsService);
    }
}
