package ru.ylab.repository.impl;

import org.junit.jupiter.api.Test;
import ru.ylab.CommonContainerBasedTest;
import ru.ylab.entity.MeterType;
import ru.ylab.repository.MeterTypeRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class JdbcMeterTypeRepositoryTest extends CommonContainerBasedTest {
    private final MeterTypeRepository meterTypeRepository = new JdbcMeterTypeRepository(dbConnectionFactory);

    @Test
    void testGetById_whenSave_thenReturnCorrectData() {
        var newTypeName = "Electricity";
        var meterType = MeterType.builder().typeName(newTypeName).build();
        assertFalse(meterTypeRepository.checkExistsByName(newTypeName));

        meterTypeRepository.save(meterType);
        assertTrue(meterTypeRepository.checkExistsByName(newTypeName));

        var allMeterTypeModels = meterTypeRepository.getAll();
        var newTypeModelOptional =
                allMeterTypeModels.stream().filter(m -> m.typeName().equals(newTypeName)).findFirst();
        assertTrue(newTypeModelOptional.isPresent());
    }

    @Test
    void testGetAll_whenSave_thenReturnCorrectData() {
        var meterType1 = MeterType.builder().typeName("Water").build();
        var meterType2 = MeterType.builder().typeName("Gas").build();

        var allMeterTypeModelsSizeBeforeSave = meterTypeRepository.getAll().size();

        meterTypeRepository.save(Set.of(meterType1, meterType2));

        var allMeterTypeModelsAfterSave = meterTypeRepository.getAll();
        assertEquals(allMeterTypeModelsSizeBeforeSave + 2, allMeterTypeModelsAfterSave.size());

        assertTrue(allMeterTypeModelsAfterSave.stream().anyMatch(m -> m.typeName().equals("Water")));
        assertTrue(allMeterTypeModelsAfterSave.stream().anyMatch(m -> m.typeName().equals("Gas")));
    }

    @Test
    void testSave_whenCorrectData_thenExistsInDatabase() {
        var newTypeName = "Air";
        var meterType = MeterType.builder().typeName(newTypeName).build();
        assertFalse(meterTypeRepository.checkExistsByName(newTypeName));

        meterTypeRepository.save(meterType);
        assertTrue(meterTypeRepository.checkExistsByName(newTypeName));
    }

    @Test
    void testSaveAll_whenCorrectData_thenExistsInDatabase() {
        var meterType1 = MeterType.builder().typeName("Petrol").build();
        var meterType2 = MeterType.builder().typeName("Diesel").build();

        var allMeterTypeModelsSizeBeforeSave = meterTypeRepository.getAll().size();

        meterTypeRepository.save(Set.of(meterType1, meterType2));

        var allMeterTypeModelsAfterSave = meterTypeRepository.getAll();
        assertEquals(allMeterTypeModelsSizeBeforeSave + 2, allMeterTypeModelsAfterSave.size());
    }
}