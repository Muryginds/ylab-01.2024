package ru.ylab.repository.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.ylab.CommonContainerBasedTest;
import ru.ylab.entity.Meter;
import ru.ylab.entity.MeterType;
import ru.ylab.entity.User;
import ru.ylab.mapper.MeterTypeMapper;
import ru.ylab.mapper.UserMapper;
import ru.ylab.repository.MeterRepository;
import ru.ylab.repository.MeterTypeRepository;
import ru.ylab.repository.UserRepository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcMeterRepositoryTest extends CommonContainerBasedTest {
    private static MeterRepository meterRepository;
    private static MeterTypeRepository meterTypeRepository;
    private static UserRepository userRepository;

    @BeforeAll
    static void setUp() {
        meterRepository = new JdbcMeterRepository(dbConnectionFactory);
        meterTypeRepository = new JdbcMeterTypeRepository(dbConnectionFactory);
        userRepository = new JdbcUserRepository(dbConnectionFactory);

        createTestMeterTypes();
        createTestUsers();
        createTestMeters();
    }

    @Test
    void testGetByUserId_whenExists_thenReturnCorrectSetOfMeters() {
        var userId = 2L;
        var meterModels = meterRepository.getByUserId(userId);

        assertEquals(2, meterModels.size());
        assertTrue(meterModels.stream().allMatch(meter -> meter.userId().equals(userId)));
    }

    @Test
    void testSave_whenCorrectData_thenSaveMeterCorrectly() {
        var userModel = userRepository.findUserByName("user2").orElseThrow();
        var electricityMeterType = meterTypeRepository.findById(1L).orElseThrow();

        var newMeter = Meter.builder()
                .factoryNumber("FactoryNumber1")
                .user(UserMapper.MAPPER.toUser(userModel))
                .meterType(MeterTypeMapper.MAPPER.toMeterType(electricityMeterType))
                .build();

        meterRepository.save(newMeter);
        var savedMeterOptional = meterRepository.getByUserId(userModel.id()).stream()
                .filter(m -> m.meterTypeId().equals(1L)).findFirst();

        assertTrue(savedMeterOptional.isPresent());

        var savedMeter = savedMeterOptional.get();
        assertEquals(newMeter.getFactoryNumber(), savedMeter.factoryNumber());
        assertEquals(newMeter.getUser().getId(), savedMeter.userId());
        assertEquals(newMeter.getMeterType().getId(), savedMeter.meterTypeId());
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
}