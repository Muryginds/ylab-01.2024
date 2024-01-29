package ru.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.ylab.entity.Meter;
import ru.ylab.entity.MeterType;
import ru.ylab.entity.Submission;
import ru.ylab.entity.User;
import ru.ylab.enumerated.UserRole;
import ru.ylab.exception.MeterNotFoundException;
import ru.ylab.exception.SubmissionExistsException;
import ru.ylab.dto.SubmissionDTO;
import ru.ylab.dto.request.SubmissionRequestDTO;
import ru.ylab.mapper.SubmissionMapper;
import ru.ylab.repository.SubmissionRepository;

import java.time.LocalDate;
import java.util.*;

class SubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

    @Mock
    private MeterReadingsService meterReadingsService;

    @Mock
    private MeterService meterService;

    @Mock
    private UserService userService;

    @Mock
    private AuditionEventService auditionEventService;

    @InjectMocks
    private SubmissionService submissionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllByUserId_whenCurrentUserHasPermission_thenReturnSubmissionDTOs() {
        long adminUserId = 1L;
        long targetUserId = 2L;
        User adminUser = User.builder().id(adminUserId).name("admin").password("admin").role(UserRole.ADMIN).build();
        User targetUser = User.builder().id(targetUserId).name("user").password("user").role(UserRole.USER).build();
        Mockito.when(userService.getCurrentUser()).thenReturn(adminUser);
        Mockito.when(userService.getUserById(targetUserId)).thenReturn(targetUser);

        Submission submission1 = Submission.builder().id(1L).user(targetUser).date(LocalDate.now()).build();
        Submission submission2 = Submission.builder().id(2L).user(targetUser).date(LocalDate.now()).build();
        Set<Submission> submissions = new HashSet<>(Arrays.asList(submission1, submission2));
        Mockito.when(submissionRepository.getByUserId(targetUserId)).thenReturn(submissions);

        Collection<SubmissionDTO> result = submissionService.getAllByUserId(targetUserId);

        Collection<SubmissionDTO> expected = SubmissionMapper.MAPPER.toSubmissionDTOs(submissions);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testFindLastSubmissionByUserId_whenUserHasPermission_thenReturnOptionalSubmission() {
        long adminUserId = 1L;
        long targetUserId = 2L;
        User adminUser = User.builder().id(adminUserId).name("admin").password("admin").role(UserRole.ADMIN).build();
        User targetUser = User.builder().id(targetUserId).name("user").password("user").role(UserRole.USER).build();
        Mockito.when(userService.getCurrentUser()).thenReturn(adminUser);
        Mockito.when(userService.getUserById(targetUserId)).thenReturn(targetUser);

        Submission submission = Submission.builder().id(1L).user(targetUser).date(LocalDate.now()).build();
        Mockito.when(submissionRepository.findLastByUserId(targetUserId)).thenReturn(Optional.of(submission));

        Optional<Submission> result = submissionService.findLastSubmissionByUserId(targetUserId);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(submission, result.get());
    }

    @Test
    void testSave_whenSubmissionExistsByDate_throwSubmissionExistsException() {
        long userId = 1L;
        LocalDate date = LocalDate.now();
        User user = User.builder().id(userId).name("user").password("user").role(UserRole.USER).build();
        SubmissionRequestDTO requestDTO = new SubmissionRequestDTO(Collections.emptyMap());
        Submission existingSubmission = Submission.builder().user(user).date(date).build();
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(submissionRepository.checkExistsByUserIdAndDate(userId, date)).thenReturn(true);
        Mockito.when(submissionRepository.findLastByUserId(userId)).thenReturn(Optional.of(existingSubmission));

        Assertions.assertThrows(SubmissionExistsException.class, () -> submissionService.save(requestDTO));
    }

    @Test
    void testSave_whenMeterNotFound_throwMeterNotFoundException() {
        long userId = 1L;
        LocalDate date = LocalDate.now();
        User user = User.builder().id(userId).name("user").password("user").role(UserRole.USER).build();
        SubmissionRequestDTO requestDTO = new SubmissionRequestDTO(Collections.singletonMap(1L, 10L));
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(submissionRepository.checkExistsByUserIdAndDate(userId, date)).thenReturn(false);
        Mockito.when(meterService.getMetersByUserId(userId)).thenReturn(Collections.emptySet());

        Assertions.assertThrows(MeterNotFoundException.class, () -> submissionService.save(requestDTO));
    }

    @Test
    void testSave_whenValid_thenDoNothing() {
        long userId = 1L;
        LocalDate date = LocalDate.now();
        User user = User.builder().id(userId).name("user").password("user").role(UserRole.USER).build();
        MeterType meterType = MeterType.builder().typeName("Electricity").build();
        Meter meter = Meter.builder().factoryNumber("123456789").user(user).type(meterType).build();
        SubmissionRequestDTO requestDTO = new SubmissionRequestDTO(Collections.singletonMap(meter.getId(), 10L));
        Mockito.when(userService.getCurrentUser()).thenReturn(user);
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(submissionRepository.checkExistsByUserIdAndDate(userId, date)).thenReturn(false);
        Mockito.when(meterService.getMetersByUserId(userId)).thenReturn(Collections.singleton(meter));

        Assertions.assertDoesNotThrow(() -> submissionService.save(requestDTO));
    }
}
