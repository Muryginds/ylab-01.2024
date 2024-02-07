package ru.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.ylab.dto.MeterTypeDTO;
import ru.ylab.entity.MeterType;
import ru.ylab.exception.MeterTypeExistException;
import ru.ylab.exception.MeterTypeNotFoundException;
import ru.ylab.mapper.MeterTypeMapper;
import ru.ylab.model.MeterTypeModel;
import ru.ylab.repository.MeterTypeRepository;

import java.util.List;
import java.util.Optional;

class MeterTypeServiceTest {

    @Mock
    private MeterTypeRepository meterTypeRepository;

    @Mock
    private AuditionEventService auditionEventService;

    @Mock
    private UserService userService;

    @InjectMocks
    private MeterTypeService meterTypeService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveMeterType_whenNonExistingType_thenDoNothing() {
        String typeName = "Electricity";
        Mockito.when(meterTypeRepository.checkExistsByName(typeName)).thenReturn(false);

        meterTypeService.save(typeName);

        Mockito.verify(meterTypeRepository, Mockito.times(1)).save(Mockito.any(MeterType.class));
        Mockito.verify(auditionEventService, Mockito.times(1)).save(Mockito.any());
    }

    @Test
    void testSaveMeterType_whenExistingType_thenThrowMeterTypeExistException() {
        String typeName = "Electricity";
        Mockito.when(meterTypeRepository.checkExistsByName(typeName)).thenReturn(true);

        Assertions.assertThrows(MeterTypeExistException.class, () -> meterTypeService.save(typeName));
        Mockito.verify(meterTypeRepository, Mockito.times(0)).save(Mockito.any(MeterType.class));
        Mockito.verify(auditionEventService, Mockito.times(0)).save(Mockito.any());
    }

    @Test
    void testGetById_whenExistingType_thenReturnMeterTypeDTO() {
        long meterTypeId = 1L;
        var meterTypeModel = MeterTypeModel.builder().id(meterTypeId).typeName("Electricity").build();
        Mockito.when(meterTypeRepository.findById(meterTypeId)).thenReturn(Optional.of(meterTypeModel));

        MeterTypeDTO result = meterTypeService.getMeterTypeDTOById(meterTypeId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(meterTypeModel.typeName(), result.typeName());
    }

    @Test
    void testGetById_whenNonExistingType_thenThrowMeterTypeNotFoundException() {
        long meterTypeId = 1L;
        Mockito.when(meterTypeRepository.findById(meterTypeId)).thenReturn(Optional.empty());

        Assertions.assertThrows(MeterTypeNotFoundException.class, () -> meterTypeService.getMeterTypeDTOById(meterTypeId));
    }

    @Test
    void testGetAll_whenTypesExist_thenReturnCollectionOfMeterTypes() {
        var typeModel1 = MeterTypeModel.builder().typeName("Electricity").build();
        var typeModel2 = MeterTypeModel.builder().typeName("Water").build();
        var types = List.of(
                MeterTypeMapper.MAPPER.toMeterType(typeModel1),
                MeterTypeMapper.MAPPER.toMeterType(typeModel2)
        );
        var typeModels = List.of(typeModel1, typeModel2);
        Mockito.when(meterTypeRepository.getAll()).thenReturn(typeModels);

        var result = meterTypeService.getAll();

        Assertions.assertEquals(types, result);
    }

    @Test
    void testCheckExistsByName_whenExistingType_thenReturnTrue() {
        String typeName = "Electricity";
        Mockito.when(meterTypeRepository.checkExistsByName(typeName)).thenReturn(true);

        boolean result = meterTypeService.checkExistsByName(typeName);

        Assertions.assertTrue(result);
    }

    @Test
    void testCheckExistsByName_whenNonExistingType_thenReturnFalse() {
        String typeName = "Electricity";
        Mockito.when(meterTypeRepository.checkExistsByName(typeName)).thenReturn(false);

        boolean result = meterTypeService.checkExistsByName(typeName);

        Assertions.assertFalse(result);
    }
}
