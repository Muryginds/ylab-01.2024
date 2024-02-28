package io.ylab.backend.repository.impl;

import io.ylab.backend.CommonIntegrationContainerBasedTest;
import io.ylab.commons.entity.Submission;
import io.ylab.commons.entity.User;
import io.ylab.commons.enumerated.UserRole;
import io.ylab.backend.repository.SubmissionRepository;
import io.ylab.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@Transactional
class JdbcSubmissionRepositoryIntegrationTest extends CommonIntegrationContainerBasedTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SubmissionRepository submissionRepository;

    @BeforeEach
    void setUp() {
        createTestData();
    }

    @Test
    void testGetByUserId_whenSubmissionExists_thenReturnCorrectSetOfSubmissions() {
        var user = userRepository.findUserByName("user1");
        assertThat(user).isPresent();

        var userId = user.get().id();
        var submissionModels = submissionRepository.getByUserId(userId);

        assertThat(submissionModels).hasSize(2)
                        .allMatch(submission -> submission.userId().equals(userId));
    }

    @Test
    void testGetById_whenExistingSubmission_thenReturnSubmissionModel() {
        var user = userRepository.findUserByName("user1");
        assertThat(user).isPresent();

        var submissions = submissionRepository.getByUserId(user.get().id());
        assertThat(submissions).isNotEmpty();

        var existingSubmissionId = submissions.stream().findFirst().get().id();
        var submissionModelOptional = submissionRepository.getById(existingSubmissionId);

        assertThat(submissionModelOptional).isPresent();
        assertThat(submissionModelOptional.get().id()).isEqualTo(existingSubmissionId);
    }

    @Test
    void testGetById_whenNonExistingSubmission_thenReturnEmptyOptional() {
        var nonExistingSubmissionId = 999L;
        var submissionModelOptional = submissionRepository.getById(nonExistingSubmissionId);

        assertThat(submissionModelOptional).isEmpty();
    }

    @Test
    void testCheckExistsByUserIdAndDate_whenExistingSubmission_thenReturnTrue() {
        var user = userRepository.findUserByName("user1");
        assertThat(user).isPresent();

        var existingUserId = user.get().id();
        var existingDate = LocalDate.of(2021, 12, 1);
        assertThat(submissionRepository.checkExistsByUserIdAndDate(existingUserId, existingDate)).isTrue();
    }

    @Test
    void testCheckExistsByUserIdAndDate_whenNonExistingSubmission_thenReturnFalse() {
        var nonExistingUserId = 999L;
        var nonExistingDate = LocalDate.of(2021, 12, 1);
        assertThat(submissionRepository.checkExistsByUserIdAndDate(nonExistingUserId, nonExistingDate)).isFalse();
    }

    @Test
    void testFindSubmissionByUserIdAndDate_whenExistingSubmission_thenReturnSubmissionModel() {
        var user = userRepository.findUserByName("user1");
        assertThat(user).isPresent();

        var existingUserId = user.get().id();
        var existingDate = LocalDate.of(2021, 12, 1);
        var submissionModelOptional = submissionRepository.findSubmissionByUserIdAndDate(existingUserId, existingDate);

        assertThat(submissionModelOptional).isPresent();

        var submissionModel = submissionModelOptional.get();
        assertThat(submissionModel.userId()).isEqualTo(existingUserId);
        assertThat(submissionModel.date()).isEqualTo(existingDate);
    }

    @Test
    void testFindSubmissionByUserIdAndDate_NonExistingSubmission_ReturnsEmptyOptional() {
        var nonExistingUserId = 999L;
        var nonExistingDate = LocalDate.of(2021, 12, 1);
        var submissionModelOptional = submissionRepository.findSubmissionByUserIdAndDate(nonExistingUserId, nonExistingDate);

        assertTrue(submissionModelOptional.isEmpty());
    }

    @Test
    void testFindLastSubmissionByUserId_whenExistingSubmission_thenReturnLastSubmissionModel() {
        var user = userRepository.findUserByName("user1");
        assertThat(user).isPresent();

        var existingUserId = user.get().id();
        var lastSubmissionModelOptional = submissionRepository.findLastSubmissionByUserId(existingUserId);

        assertThat(lastSubmissionModelOptional).isPresent();

        var lastSubmissionModel = lastSubmissionModelOptional.get();
        assertThat(lastSubmissionModel.userId()).isEqualTo(existingUserId);
    }

    @Test
    void testFindLastSubmissionByUserId_whenNonExistingSubmission_thenReturnEmptyOptional() {
        var nonExistingUserId = 999L;
        var lastSubmissionModelOptional = submissionRepository.findLastSubmissionByUserId(nonExistingUserId);

        assertThat(lastSubmissionModelOptional).isEmpty();
    }

    private void createTestData() {
        var user = User.builder().name("user1").password("password1").role(UserRole.USER).build();
        var submission1 =
                Submission.builder().user(user).date(LocalDate.of(2021, 12, 1)).build();
        var submission2 =
                Submission.builder().user(user).date(LocalDate.of(2021, 12, 15)).build();
        userRepository.save(user);
        submissionRepository.save(submission1);
        submissionRepository.save(submission2);
    }
}
