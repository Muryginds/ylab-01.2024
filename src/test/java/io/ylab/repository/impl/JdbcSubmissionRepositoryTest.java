package io.ylab.repository.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.ylab.CommonContainerBasedTest;
import io.ylab.entity.Submission;
import io.ylab.entity.User;
import io.ylab.enumerated.UserRole;
import io.ylab.mapper.UserMapper;
import io.ylab.repository.SubmissionRepository;
import io.ylab.repository.UserRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class JdbcSubmissionRepositoryTest extends CommonContainerBasedTest {
    private static UserRepository userRepository;
    private static SubmissionRepository submissionRepository;

    @BeforeAll
    static void setUp() {
        userRepository = new JdbcUserRepository(dbConnectionFactory);
        submissionRepository = new JdbcSubmissionRepository(dbConnectionFactory);
        createTestUsers();
        createTestSubmissions();
    }

    @Test
    void testGetByUserId_whenSubmissionExists_thenReturnCorrectSetOfSubmissions() {
        var userId = 2L;
        var submissionModels = submissionRepository.getByUserId(userId);

        assertEquals(2, submissionModels.size());
        assertTrue(submissionModels.stream().allMatch(submission -> submission.userId().equals(userId)));
    }

    @Test
    void testGetById_whenExistingSubmission_thenReturnSubmissionModel() {
        var existingSubmissionId = 2L;
        var submissionModelOptional = submissionRepository.getById(existingSubmissionId);

        assertTrue(submissionModelOptional.isPresent());
        assertEquals(existingSubmissionId, submissionModelOptional.get().id());
    }

    @Test
    void testGetById_whenNonExistingSubmission_thenReturnEmptyOptional() {
        var nonExistingSubmissionId = 999L;
        var submissionModelOptional = submissionRepository.getById(nonExistingSubmissionId);

        assertTrue(submissionModelOptional.isEmpty());
    }

    @Test
    void testCheckExistsByUserIdAndDate_whenExistingSubmission_thenReturnTrue() {
        var existingUserId = 2L;
        var existingDate = LocalDate.of(2021, 12, 1);
        assertTrue(submissionRepository.checkExistsByUserIdAndDate(existingUserId, existingDate));
    }

    @Test
    void testCheckExistsByUserIdAndDate_whenNonExistingSubmission_thenReturnFalse() {
        var nonExistingUserId = 999L;
        var nonExistingDate = LocalDate.of(2021, 12, 1);
        assertFalse(submissionRepository.checkExistsByUserIdAndDate(nonExistingUserId, nonExistingDate));
    }

    @Test
    void testFindSubmissionByUserIdAndDate_whenExistingSubmission_thenReturnSubmissionModel() {
        var existingUserId = 2L;
        var existingDate = LocalDate.of(2021, 12, 1);
        var submissionModelOptional = submissionRepository.findSubmissionByUserIdAndDate(existingUserId, existingDate);

        assertTrue(submissionModelOptional.isPresent());

        var submissionModel = submissionModelOptional.get();
        assertEquals(existingUserId, submissionModel.userId());
        assertEquals(existingDate, submissionModel.date());
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
        var existingUserId = 2L;
        var lastSubmissionModelOptional = submissionRepository.findLastSubmissionByUserId(existingUserId);

        assertTrue(lastSubmissionModelOptional.isPresent());

        var lastSubmissionModel = lastSubmissionModelOptional.get();
        assertEquals(existingUserId, lastSubmissionModel.userId());
    }

    @Test
    void testFindLastSubmissionByUserId_whenNonExistingSubmission_thenReturnEmptyOptional() {
        var nonExistingUserId = 999L;
        var lastSubmissionModelOptional = submissionRepository.findLastSubmissionByUserId(nonExistingUserId);

        assertTrue(lastSubmissionModelOptional.isEmpty());
    }

    private static void createTestUsers() {
        userRepository.save(User.builder().name("user1").password("password1").role(UserRole.USER).build());
    }

    private static void createTestSubmissions() {
        var user1 = UserMapper.MAPPER.toUser(userRepository.findUserByName("user1").orElseThrow());
        submissionRepository.save(Submission.builder().user(user1).date(LocalDate.of(2021, 12, 1)).build());
        submissionRepository.save(Submission.builder().user(user1).date(LocalDate.of(2021, 12, 15)).build());
    }
}
