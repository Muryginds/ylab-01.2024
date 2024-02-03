package ru.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.ylab.dto.request.SubmissionRequestDTO;
import ru.ylab.entity.Meter;
import ru.ylab.entity.MeterType;
import ru.ylab.entity.User;
import ru.ylab.enumerated.UserRole;
import ru.ylab.exception.MeterNotFoundException;
import ru.ylab.exception.SubmissionExistsException;

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
        var requestDTO = new SubmissionRequestDTO(Collections.emptyMap());
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
        var requestDTO = new SubmissionRequestDTO(Collections.singletonMap(1L, 10L));
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
        var requestDTO = new SubmissionRequestDTO(Collections.singletonMap(meter.getId(), 10L));
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(submissionService.checkExistsByUserIdAndDate(userId, date)).thenReturn(false);
        Mockito.when(meterService.getMetersByUserId(userId)).thenReturn(Collections.singleton(meter));

        Assertions.assertDoesNotThrow(() -> readingsRecordingService.saveNewSubmission(requestDTO));
    }
}