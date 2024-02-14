package io.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import io.ylab.dto.request.ReadingRequestDTO;
import io.ylab.dto.request.NewReadingsSubmissionRequestDTO;
import io.ylab.entity.Meter;
import io.ylab.entity.MeterType;
import io.ylab.entity.User;
import io.ylab.enumerated.UserRole;
import io.ylab.exception.MeterNotFoundException;
import io.ylab.exception.SubmissionExistsException;

import java.time.LocalDate;
import java.util.Collections;

class ReadingsRecordingServiceTest {

    @Mock
    private MeterReadingsService meterReadingsService;

    @Mock
    private MeterService meterService;

    @Mock
    private SubmissionService submissionService;

    @Mock
    private UserService userService;

    @Mock
    private AuditionEventService auditionEventService;

    @InjectMocks
    private ReadingsRecordingService readingsRecordingService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveNewSubmission_whenSubmissionExistsByDate_throwSubmissionExistsException() {
        var userId = 1L;
        var date = LocalDate.now();
        var user = User.builder().id(userId).name("user").password("user").role(UserRole.USER).build();
        var requestDTO = new NewReadingsSubmissionRequestDTO(Collections.emptyList());
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(submissionService.checkExistsByUserIdAndDate(userId, date)).thenReturn(true);

        Assertions.assertThrows(SubmissionExistsException.class,
                () -> readingsRecordingService.saveNewSubmission(requestDTO));
    }

    @Test
    void testSaveNewSubmission_whenMeterNotFound_throwMeterNotFoundException() {
        var userId = 1L;
        var date = LocalDate.now();
        var user = User.builder().id(userId).name("user").password("user").role(UserRole.USER).build();
        var requestDTO = new NewReadingsSubmissionRequestDTO(
                Collections.singletonList(ReadingRequestDTO.builder().meterId(1L).value(10L).build())
        );
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(submissionService.checkExistsByUserIdAndDate(userId, date)).thenReturn(false);
        Mockito.when(meterService.getMetersByUserId(userId)).thenReturn(Collections.emptySet());

        Assertions.assertThrows(MeterNotFoundException.class,
                () -> readingsRecordingService.saveNewSubmission(requestDTO));
    }

    @Test
    void testSaveNewSubmission_whenValid_thenDoNothing() {
        var userId = 1L;
        var date = LocalDate.now();
        var user = User.builder().id(userId).name("user").password("user").role(UserRole.USER).build();
        var meterType = MeterType.builder().typeName("Electricity").build();
        var meter = Meter.builder().factoryNumber("123456789").user(user).meterType(meterType).build();
        var requestDTO = new NewReadingsSubmissionRequestDTO(
                Collections.singletonList(ReadingRequestDTO.builder().meterId(meter.getId()).value(10L).build())
        );
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(submissionService.checkExistsByUserIdAndDate(userId, date)).thenReturn(false);
        Mockito.when(meterService.getMetersByUserId(userId)).thenReturn(Collections.singleton(meter));

        Assertions.assertDoesNotThrow(() -> readingsRecordingService.saveNewSubmission(requestDTO));
    }
}