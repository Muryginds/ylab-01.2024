package io.ylab.service;

import io.ylab.entity.Submission;
import io.ylab.entity.User;
import io.ylab.exception.NoPermissionException;
import io.ylab.exception.NoSubmissionException;
import io.ylab.mapper.SubmissionMapper;
import io.ylab.model.SubmissionModel;
import io.ylab.repository.SubmissionRepository;
import io.ylab.utils.CurrentUserUtils;
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
import java.util.Collection;
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
        Long userId = 1L;
        var user = User.builder().id(userId).build();
        var submission = Submission.builder().user(user).build();
        var submissionModel = SubmissionModel.builder().build();
        when(CurrentUserUtils.getCurrentUser()).thenReturn(user);
        when(userService.getUserById(userId)).thenReturn(submission.getUser());
        when(submissionRepository.getByUserId(userId)).thenReturn(Collections.singletonList(submissionModel));
        when(submissionMapper.toSubmission(any(), any())).thenReturn(submission);

        Collection<Submission> result = submissionService.getAll(userId);

        assertEquals(Collections.singletonList(submission), new ArrayList<>(result));
    }

    @Test
    void testGetSubmission_NoPermission() {
        LocalDate date = LocalDate.now();
        Long userId = 1L;
        var user = User.builder().build();
        when(CurrentUserUtils.getCurrentUser()).thenReturn(user);

        assertThrows(NoPermissionException.class, () -> submissionService.getSubmission(date, userId));
    }

    @Test
    void testGetSubmission_NoSubmission() {
        // Arrange
        LocalDate date = LocalDate.now();
        Long userId = 1L;
        var user = User.builder().id(userId).build();
        var submission = Submission.builder().user(user).build();
        when(CurrentUserUtils.getCurrentUser()).thenReturn(submission.getUser());
        when(userService.getUserById(userId)).thenReturn(submission.getUser());
        when(submissionRepository.findSubmissionByUserIdAndDate(userId, date)).thenReturn(Optional.empty());

        assertThrows(NoSubmissionException.class, () -> submissionService.getSubmission(date, userId));
    }
}
