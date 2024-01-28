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
import ru.ylab.in.dto.MeterDTO;
import ru.ylab.mapper.MeterMapper;
import ru.ylab.repository.MeterRepository;
import ru.ylab.repository.MeterTypeRepository;

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

class MeterServiceTest {

    @Mock
    private MeterRepository meterRepository;

    @Mock
    private MeterTypeRepository meterTypeRepository;

    @InjectMocks
    private MeterService meterService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveMeter_whenCorrect_thenDoNothing() {
        MeterType typeElectricity = MeterType.builder().typeName("Electricity").build();
        Meter meter = Meter.builder().factoryNumber("123456789012").type(typeElectricity).build();

        meterService.save(meter);

        Mockito.verify(meterRepository, Mockito.times(1)).save(meter);
    }

    @Test
    void testSaveMeter_whenCorrectInCollection_thenDoNothing() {
        MeterType typeElectricity = MeterType.builder().typeName("Electricity").build();
        MeterType typeGas = MeterType.builder().typeName("Gas").build();
        Meter meter1 = Meter.builder().factoryNumber("123456789012").type(typeElectricity).build();
        Meter meter2 = Meter.builder().factoryNumber("987654321098").type(typeGas).build();
        Collection<Meter> meters = Arrays.asList(meter1, meter2);

        meterService.save(meters);

        Mockito.verify(meterRepository, Mockito.times(1)).save(meters);
    }

    @Test
    void testGetMeterById_whenCorrect_thenReturnMeter() {
        long meterId = 1L;
        MeterType typeElectricity = MeterType.builder().typeName("Electricity").build();
        Meter meter = Meter.builder().id(meterId).factoryNumber("123456789012").type(typeElectricity).build();
        Mockito.when(meterRepository.findById(meterId)).thenReturn(Optional.of(meter));

        Meter result = meterService.getById(meterId);

        Assertions.assertEquals(meter, result);
    }

    @Test
    void testGetMeterDTOsByUserId_whenCorrect_thenReturnCollectionOfMeterDTOs() {
        long userId = 1L;
        MeterType typeElectricity = MeterType.builder().typeName("Electricity").build();
        MeterType typeGas = MeterType.builder().typeName("Gas").build();
        Meter meter1 = Meter.builder().factoryNumber("123456789012").type(typeElectricity).build();
        Meter meter2 = Meter.builder().factoryNumber("987654321098").type(typeGas).build();
        Set<Meter> meters = Set.of(meter1, meter2);
        Mockito.when(meterRepository.getByUserId(userId)).thenReturn(meters);

        Collection<MeterDTO> result = meterService.getMeterDTOsByUserId(userId);

        Collection<MeterDTO> expected = MeterMapper.MAPPER.toMeterDTOs(meters);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testGetMetersByUserId_whenCorrect_thenReturnCollectionOfMeters() {
        long userId = 1L;
        MeterType typeElectricity = MeterType.builder().typeName("Electricity").build();
        MeterType typeGas = MeterType.builder().typeName("Gas").build();
        Meter meter1 = Meter.builder().factoryNumber("123456789012").type(typeElectricity).build();
        Meter meter2 = Meter.builder().factoryNumber("987654321098").type(typeGas).build();
        Set<Meter> meters = Set.of(meter1, meter2);
        Mockito.when(meterRepository.getByUserId(userId)).thenReturn(meters);

        Collection<Meter> result = meterService.getMetersByUserId(userId);

        Assertions.assertEquals(meters, result);
    }
}