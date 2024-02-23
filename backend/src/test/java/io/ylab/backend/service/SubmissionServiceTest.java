package io.ylab.backend.service;

import io.ylab.backend.entity.Submission;
import io.ylab.backend.entity.User;
import io.ylab.backend.exception.NoPermissionException;
import io.ylab.backend.exception.NoSubmissionException;
import io.ylab.backend.mapper.SubmissionMapper;
import io.ylab.backend.model.SubmissionModel;
import io.ylab.backend.repository.SubmissionRepository;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceTest {
    @Mock
    private SubmissionRepository submissionRepository;
    @Mock
    private UserService userService;
    @Mock
    private SubmissionMapper submissionMapper;
    @InjectMocks
    private SubmissionService submissionService;

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
    void testGetAll() {
        var userId = 1L;
        var user = User.builder().id(userId).build();
        var submission = Submission.builder().user(user).build();
        var submissionModel = SubmissionModel.builder().build();
        when(CurrentUserUtils.getCurrentUser()).thenReturn(user);
        when(userService.getUserById(userId)).thenReturn(submission.getUser());
        when(submissionRepository.getByUserId(userId)).thenReturn(Collections.singletonList(submissionModel));
        when(submissionMapper.toSubmission(any(), any())).thenReturn(submission);

        var result = submissionService.getAll(userId);

        assertEquals(Collections.singletonList(submission), new ArrayList<>(result));
    }

    @Test
    void testGetSubmission_NoPermission() {
        var date = LocalDate.now();
        var userId = 1L;
        var user = User.builder().build();
        when(CurrentUserUtils.getCurrentUser()).thenReturn(user);

        assertThrows(NoPermissionException.class, () -> submissionService.getSubmission(date, userId));
    }

    @Test
    void testGetSubmission_NoSubmission() {
        var date = LocalDate.now();
        var userId = 1L;
        var user = User.builder().id(userId).build();
        var submission = Submission.builder().user(user).build();
        when(CurrentUserUtils.getCurrentUser()).thenReturn(submission.getUser());
        when(userService.getUserById(userId)).thenReturn(submission.getUser());
        when(submissionRepository.findSubmissionByUserIdAndDate(userId, date)).thenReturn(Optional.empty());

        assertThrows(NoSubmissionException.class, () -> submissionService.getSubmission(date, userId));
    }
}
