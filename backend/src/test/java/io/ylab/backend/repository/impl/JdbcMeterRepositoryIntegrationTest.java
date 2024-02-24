package io.ylab.backend.repository.impl;

import io.ylab.backend.CommonIntegrationContainerBasedTest;
import io.ylab.commons.entity.Meter;
import io.ylab.commons.entity.MeterType;
import io.ylab.commons.entity.User;
import io.ylab.backend.mapper.MeterTypeMapper;
import io.ylab.backend.mapper.UserMapper;
import io.ylab.backend.repository.MeterRepository;
import io.ylab.backend.repository.MeterTypeRepository;
import io.ylab.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
class JdbcMeterRepositoryIntegrationTest extends CommonIntegrationContainerBasedTest {
    @Autowired
    private MeterRepository meterRepository;
    @Autowired
    private MeterTypeRepository meterTypeRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MeterTypeMapper meterTypeMapper;

    @BeforeEach
    void setUp() {
        createTestData();
    }

    @Test
    void testGetByUserId_whenExists_thenReturnCorrectSetOfMeters() {
        var user = userRepository.findUserByName("user1");
        assertTrue(user.isPresent());
        var userId = user.get().id();
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

    private void createTestData() {
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