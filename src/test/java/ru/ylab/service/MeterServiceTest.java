package ru.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.ylab.entity.Meter;
import ru.ylab.entity.MeterType;
import ru.ylab.entity.User;
import ru.ylab.mapper.MeterMapper;
import ru.ylab.repository.MeterRepository;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;

class MeterServiceTest {

    @Mock
    private MeterRepository meterRepository;

    @Mock
    private MeterTypeService meterTypeService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MeterService meterService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveMeter_whenCorrect_thenDoNothing() {
        var typeElectricity = MeterType.builder().typeName("Electricity").build();
        var meter = Meter.builder().factoryNumber("123456789012").meterType(typeElectricity).build();

        meterService.save(meter);

        Mockito.verify(meterRepository, Mockito.times(1)).save(meter);
    }

    @Test
    void testSaveMeter_whenCorrectInCollection_thenDoNothing() {
        var typeElectricity = MeterType.builder().typeName("Electricity").build();
        var typeGas = MeterType.builder().typeName("Gas").build();
        var meter1 = Meter.builder().factoryNumber("123456789012").meterType(typeElectricity).build();
        var meter2 = Meter.builder().factoryNumber("987654321098").meterType(typeGas).build();
        var meters = Arrays.asList(meter1, meter2);

        meterService.save(meters);

        Mockito.verify(meterRepository, Mockito.times(1)).save(meters);
    }

    @Test
    void testGetById_whenCorrect_thenReturnMeter() {
        var meterId = 1L;
        var expectedMeter = Meter.builder().id(meterId).factoryNumber("123456789012").build();
        var meterModel = MeterMapper.MAPPER.toMeterModel(expectedMeter);
        Mockito.when(meterRepository.findById(meterId)).thenReturn(Optional.of(meterModel));

        Meter result = meterService.getById(meterId);

        Assertions.assertEquals(expectedMeter.getId(), result.getId());
    }

    @Test
    void testGetMeterDTOsByUserId_whenCorrect_thenReturnCollectionOfMeterDTOs() {
        var userId = 1L;
        var meter1 = Meter.builder().factoryNumber("123456789012").build();
        var meter2 = Meter.builder().factoryNumber("987654321098").build();
        var meterModels = Set.of(
                MeterMapper.MAPPER.toMeterModel(meter1),
                MeterMapper.MAPPER.toMeterModel(meter2)
        );
        var meterDTOs = Set.of(
                MeterMapper.MAPPER.toMeterDTO(meter1),
                MeterMapper.MAPPER.toMeterDTO(meter2)
        );
        Mockito.when(meterRepository.getByUserId(userId)).thenReturn(meterModels);

        var result = meterService.getMeterDTOsByUserId(userId);

        Assertions.assertTrue(result.containsAll(meterDTOs));
        Assertions.assertEquals(result.size(), meterDTOs.size());
    }

    @Test
    void testGetMetersByUserId_whenCorrect_thenReturnCollectionOfMeters() {
        long userId = 1L;
        var user = User.builder().id(userId).name("user").build();
        var typeElectricity = MeterType.builder().id(1L).typeName("Electricity").build();
        var typeGas = MeterType.builder().id(2L).typeName("Gas").build();
        var meter1 = Meter.builder().id(1L).user(user).factoryNumber("123456789012").meterType(typeElectricity).build();
        var meter2 = Meter.builder().id(2L).user(user).factoryNumber("987654321098").meterType(typeGas).build();
        var meterModels = Set.of(
                MeterMapper.MAPPER.toMeterModel(meter1),
                MeterMapper.MAPPER.toMeterModel(meter2)
        );
        var expectedMeters = Set.of(meter1, meter2);
        Mockito.when(userService.getUserById(userId)).thenReturn(user);
        Mockito.when(meterTypeService.getMeterTypeById(1L)).thenReturn(typeElectricity);
        Mockito.when(meterTypeService.getMeterTypeById(2L)).thenReturn(typeGas);
        Mockito.when(meterRepository.getByUserId(userId)).thenReturn(meterModels);

        var result = meterService.getMetersByUserId(userId);

        Assertions.assertTrue(result.containsAll(expectedMeters));
        Assertions.assertEquals(result.size(), expectedMeters.size());
    }
}