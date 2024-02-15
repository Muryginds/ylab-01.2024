package ru.ylab.repository.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.ylab.CommonContainerBasedTest;
import ru.ylab.entity.*;
import ru.ylab.mapper.MeterMapper;
import ru.ylab.mapper.MeterTypeMapper;
import ru.ylab.mapper.SubmissionMapper;
import ru.ylab.mapper.UserMapper;
import ru.ylab.repository.MeterRepository;
import ru.ylab.repository.MeterTypeRepository;
import ru.ylab.repository.SubmissionRepository;
import ru.ylab.repository.UserRepository;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcMeterReadingRepositoryTest extends CommonContainerBasedTest {
    private static JdbcMeterReadingRepository meterReadingsRepository;
    private static MeterRepository meterRepository;
    private static SubmissionRepository submissionRepository;
    private static UserRepository userRepository;
    private static MeterTypeRepository meterTypeRepository;

    @BeforeAll
    static void setUp() {
        meterReadingsRepository = new JdbcMeterReadingRepository(dbConnectionFactory);
        meterRepository = new JdbcMeterRepository(dbConnectionFactory);
        submissionRepository = new JdbcSubmissionRepository(dbConnectionFactory);
        userRepository = new JdbcUserRepository(dbConnectionFactory);
        meterTypeRepository = new JdbcMeterTypeRepository(dbConnectionFactory);

        createTestUsers();
        createTestMeterTypes();
        createTestMeters();
        createTestSubmissions();
    }

    @Test
    void testSave_whenNoSubmissionsForDate_thenSaveCorrectly() {
        var user = UserMapper.MAPPER.toUser(userRepository.findUserByName("user1").orElseThrow());
        var date = LocalDate.of(2023, 1, 1);
        var submission = Submission.builder()
                .user(user)
                .date(date)
                .build();
        submissionRepository.save(submission);

        var savedSubmissionModel = submissionRepository.findSubmissionByUserIdAndDate(user.getId(), date);
        assertTrue(savedSubmissionModel.isPresent());

        var electricityMeterType = MeterTypeMapper.MAPPER.toMeterType(
                meterTypeRepository.findById(1L).orElseThrow()
        );
        var waterMeterType = MeterTypeMapper.MAPPER.toMeterType(
                meterTypeRepository.findById(2L).orElseThrow()
        );

        var savedSubmission = SubmissionMapper.MAPPER.toSubmission(savedSubmissionModel.get(), user);
        var meterModels = meterRepository.getByUserId(user.getId());
        var meterModel1 = meterModels.stream().filter(m -> m.meterTypeId().equals(electricityMeterType.getId())).findFirst();
        var meterModel2 = meterModels.stream().filter(m -> m.meterTypeId().equals(waterMeterType.getId())).findFirst();

        assertTrue(meterModel1.isPresent());
        assertTrue(meterModel2.isPresent());

        var meterReading1 = MeterReading.builder()
                .meter(MeterMapper.MAPPER.toMeter(meterModel1.get(), electricityMeterType, user))
                .submission(savedSubmission)
                .value(150L)
                .build();

        var meterReading2 = MeterReading.builder()
                .meter(MeterMapper.MAPPER.toMeter(meterModel2.get(), waterMeterType, user))
                .submission(savedSubmission)
                .value(100L)
                .build();

        meterReadingsRepository.save(meterReading1);
        meterReadingsRepository.save(meterReading2);

        var savedMeterReadingModels = meterReadingsRepository.getAllBySubmissionId(submission.getId());

        assertEquals(2, savedMeterReadingModels.size());
    }

    private static void createTestMeterTypes() {
        meterTypeRepository.save(MeterType.builder().typeName("Electricity").build());
        meterTypeRepository.save(MeterType.builder().typeName("Water").build());
    }

    private static void createTestUsers() {
        userRepository.save(User.builder().name("user1").password("password1").build());
        userRepository.save(User.builder().name("user2").password("password2").build());
    }

    private static void createTestMeters() {
        var electricityMeterType = meterTypeRepository.findById(1L).orElseThrow();
        var waterMeterType = meterTypeRepository.findById(2L).orElseThrow();

        var user1 = userRepository.findUserByName("user1").orElseThrow();
        var user2 = userRepository.findUserByName("user2").orElseThrow();

        meterRepository.save(Meter.builder()
                .factoryNumber("FactoryNumber1")
                .user(UserMapper.MAPPER.toUser(user1))
                .meterType(MeterTypeMapper.MAPPER.toMeterType(electricityMeterType))
                .build()
        );

        meterRepository.save(Meter.builder()
                .factoryNumber("FactoryNumber2")
                .user(UserMapper.MAPPER.toUser(user1))
                .meterType(MeterTypeMapper.MAPPER.toMeterType(waterMeterType))
                .build()
        );

        meterRepository.save(Meter.builder()
                .factoryNumber("FactoryNumber3")
                .user(UserMapper.MAPPER.toUser(user2))
                .meterType(MeterTypeMapper.MAPPER.toMeterType(waterMeterType))
                .build()
        );
    }

    private static void createTestSubmissions() {
        var user1 = UserMapper.MAPPER.toUser(userRepository.findUserByName("user1").orElseThrow());
        var user2 = UserMapper.MAPPER.toUser(userRepository.findUserByName("user2").orElseThrow());
        submissionRepository.save(Submission.builder().user(user1).date(LocalDate.of(2021, 12, 1)).build());
        submissionRepository.save(Submission.builder().user(user1).date(LocalDate.of(2021, 12, 15)).build());
        submissionRepository.save(Submission.builder().user(user2).date(LocalDate.of(2022, 1, 1)).build());
    }
}
