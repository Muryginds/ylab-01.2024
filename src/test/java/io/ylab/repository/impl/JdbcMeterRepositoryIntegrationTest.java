package io.ylab.repository.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.ylab.CommonIntegrationContainerBasedTest;
import io.ylab.entity.Meter;
import io.ylab.entity.MeterType;
import io.ylab.entity.User;
import io.ylab.mapper.MeterTypeMapper;
import io.ylab.mapper.UserMapper;
import io.ylab.repository.MeterRepository;
import io.ylab.repository.MeterTypeRepository;
import io.ylab.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcMeterRepositoryIntegrationTest extends CommonIntegrationContainerBasedTest {
    private static MeterRepository meterRepository;
    private static MeterTypeRepository meterTypeRepository;
    private static UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private MeterTypeMapper meterTypeMapper;

    @BeforeAll
    static void setUp() {
        meterRepository = new JdbcMeterRepository(getJdbcTemplate());
        meterTypeRepository = new JdbcMeterTypeRepository(getJdbcTemplate());
        userRepository = new JdbcUserRepository(getJdbcTemplate());

        createTestData();
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
                .user(userMapper.toUser(userModel))
                .meterType(meterTypeMapper.toMeterType(electricityMeterType))
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

    private static void createTestData() {
        var electricityType = MeterType.builder().typeName("Electricity").build();
        var waterType = MeterType.builder().typeName("Water").build();
        meterTypeRepository.save(electricityType);
        meterTypeRepository.save(waterType);
        var user1 = User.builder().name("user1").password("password1").build();
        var user2 = User.builder().name("user2").password("password2").build();
        userRepository.save(user1);
        userRepository.save(user2);
        var factoryNumber1 = Meter.builder().factoryNumber("FactoryNumber1")
                .user(user1).meterType(electricityType).build();
        var factoryNumber2 = Meter.builder().factoryNumber("FactoryNumber2")
                .user(user1).meterType(waterType).build();
        var factoryNumber3 = Meter.builder().factoryNumber("FactoryNumber3")
                .user(user2).meterType(waterType).build();
        meterRepository.save(factoryNumber1);
        meterRepository.save(factoryNumber2);
        meterRepository.save(factoryNumber3);
    }
}