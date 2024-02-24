package io.ylab.backend.repository.impl;

import io.ylab.commons.entity.*;
import io.ylab.backend.mapper.MeterMapper;
import io.ylab.backend.mapper.MeterTypeMapper;
import io.ylab.backend.mapper.SubmissionMapper;
import io.ylab.backend.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import io.ylab.backend.CommonIntegrationContainerBasedTest;
import io.ylab.backend.repository.MeterRepository;
import io.ylab.backend.repository.MeterTypeRepository;
import io.ylab.backend.repository.SubmissionRepository;
import io.ylab.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
class JdbcMeterReadingRepositoryIntegrationTest extends CommonIntegrationContainerBasedTest {
    @Autowired
    private JdbcMeterReadingRepository meterReadingsRepository;
    @Autowired
    private MeterRepository meterRepository;
    @Autowired
    private SubmissionRepository submissionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MeterTypeRepository meterTypeRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MeterTypeMapper meterTypeMapper;
    @Autowired
    private MeterMapper meterMapper;
    @Autowired
    private SubmissionMapper submissionMapper;


    @BeforeEach
    void setUp() {
        createTestData();
    }

    @Test
    void testSave_whenNoSubmissionsForDate_thenSaveCorrectly() {
        var user = userMapper.toUser(userRepository.findUserByName("user1").orElseThrow());
        var date = LocalDate.of(2023, 1, 1);
        var submission = Submission.builder()
                .user(user)
                .date(date)
                .build();
        submissionRepository.save(submission);

        var savedSubmissionModel = submissionRepository.findSubmissionByUserIdAndDate(user.getId(), date);
        assertTrue(savedSubmissionModel.isPresent());

        var electricityMeterType = meterTypeMapper.toMeterType(
                meterTypeRepository.findByName("Electricity").orElseThrow()
        );
        var waterMeterType = meterTypeMapper.toMeterType(
                meterTypeRepository.findByName("Water").orElseThrow()
        );

        var savedSubmission = submissionMapper.toSubmission(savedSubmissionModel.get(), user);
        var meterModels = meterRepository.getByUserId(user.getId());
        var meterModel1 = meterModels.stream().filter(m -> m.meterTypeId().equals(electricityMeterType.getId())).findFirst();
        var meterModel2 = meterModels.stream().filter(m -> m.meterTypeId().equals(waterMeterType.getId())).findFirst();

        assertTrue(meterModel1.isPresent());
        assertTrue(meterModel2.isPresent());

        var meterReading1 = MeterReading.builder()
                .meter(meterMapper.toMeter(meterModel1.get(), electricityMeterType, user))
                .submission(savedSubmission)
                .value(150L)
                .build();

        var meterReading2 = MeterReading.builder()
                .meter(meterMapper.toMeter(meterModel2.get(), waterMeterType, user))
                .submission(savedSubmission)
                .value(100L)
                .build();

        meterReadingsRepository.save(meterReading1);
        meterReadingsRepository.save(meterReading2);

        var savedMeterReadingModels = meterReadingsRepository.getAllBySubmissionId(submission.getId());

        assertEquals(2, savedMeterReadingModels.size());
    }

    private void createTestData() {
        var electricity = MeterType.builder().typeName("Electricity").build();
        var water = MeterType.builder().typeName("Water").build();
        meterTypeRepository.save(electricity);
        meterTypeRepository.save(water);

        User user1 = User.builder().name("user1").password("password1").build();
        User user2 = User.builder().name("user2").password("password2").build();
        userRepository.save(user1);
        userRepository.save(user2);

        meterRepository.save(Meter.builder().factoryNumber("FactoryNumber1")
                .user(user1).meterType(electricity).build()
        );
        meterRepository.save(Meter.builder().factoryNumber("FactoryNumber2")
                .user(user1).meterType(water).build()
        );
        meterRepository.save(Meter.builder().factoryNumber("FactoryNumber3")
                .user(user2).meterType(water).build()
        );
        var submission1 = Submission.builder().user(user1).date(LocalDate.of(2021, 12, 1)).build();
        var submission2 = Submission.builder().user(user1).date(LocalDate.of(2021, 12, 15)).build();
        var submission3 = Submission.builder().user(user2).date(LocalDate.of(2022, 1, 1)).build();
        submissionRepository.save(submission2);
        submissionRepository.save(submission1);
        submissionRepository.save(submission3);
    }
}
