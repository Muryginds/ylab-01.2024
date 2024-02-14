package io.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import io.ylab.dto.request.AllSubmissionsRequestDTO;
import io.ylab.dto.request.SubmissionRequestDTO;
import io.ylab.entity.Submission;
import io.ylab.entity.User;
import io.ylab.enumerated.UserRole;
import io.ylab.mapper.SubmissionMapper;
import io.ylab.repository.SubmissionRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.Set;

class SubmissionServiceTest {

    @Mock
    private SubmissionRepository submissionRepository;

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
        var adminUserId = 1L;
        var targetUserId = 2L;
        var adminUser = User.builder().id(adminUserId).name("admin").password("admin").role(UserRole.ADMIN).build();
        var targetUser = User.builder().id(targetUserId).name("user").password("user").role(UserRole.USER).build();
        Mockito.when(userService.getCurrentUser()).thenReturn(adminUser);
        Mockito.when(userService.getUserById(targetUserId)).thenReturn(targetUser);

        var submission1 = Submission.builder().id(1L).user(targetUser).date(LocalDate.now()).build();
        var submission2 = Submission.builder().id(2L).user(targetUser).date(LocalDate.now()).build();
        var expectedSubmissions = Set.of(submission1, submission2);
        var submissionModels = Set.of(
                SubmissionMapper.MAPPER.toSubmissionModel(submission1),
                SubmissionMapper.MAPPER.toSubmissionModel(submission2)
        );
        Mockito.when(submissionRepository.getByUserId(targetUserId)).thenReturn(submissionModels);

        var request = AllSubmissionsRequestDTO.builder().userId(targetUserId).build();
        var result = submissionService.getAll(request);

        Assertions.assertTrue(result.containsAll(expectedSubmissions));
        Assertions.assertEquals(result.size(), expectedSubmissions.size());
    }

    @Test
    void testFindLastSubmissionByUserId_whenUserHasPermission_thenReturnOptionalSubmission() {
        var adminUserId = 1L;
        var targetUserId = 2L;
        var adminUser = User.builder().id(adminUserId).name("admin").password("admin").role(UserRole.ADMIN).build();
        var targetUser = User.builder().id(targetUserId).name("user").password("user").role(UserRole.USER).build();
        Mockito.when(userService.getCurrentUser()).thenReturn(adminUser);
        Mockito.when(userService.getUserById(targetUserId)).thenReturn(targetUser);

        var expectedSubmission = Submission.builder().id(1L).user(targetUser).date(LocalDate.now()).build();
        var submissionModel = SubmissionMapper.MAPPER.toSubmissionModel(expectedSubmission);

        Mockito.when(submissionRepository.findLastSubmissionByUserId(targetUserId))
                .thenReturn(Optional.of(submissionModel));
        var request = SubmissionRequestDTO.builder().userId(targetUserId).build();
        var result = submissionService.getSubmission(request);

        Assertions.assertEquals(expectedSubmission, result);
    }
}
