package io.ylab.backend.repository.impl;

import io.ylab.backend.CommonIntegrationContainerBasedTest;
import io.ylab.backend.repository.MeterTypeRepository;
import io.ylab.commons.entity.MeterType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class JdbcMeterTypeRepositoryIntegrationTest extends CommonIntegrationContainerBasedTest {
    @Autowired
    private MeterTypeRepository meterTypeRepository;

    @Test
    void testGetById_whenSave_thenReturnCorrectData() {
        var newTypeName = "Electricity";
        var meterType = MeterType.builder().typeName(newTypeName).build();
        assertThat(meterTypeRepository.checkExistsByName(newTypeName)).isFalse();

        meterTypeRepository.save(meterType);
        assertThat(meterTypeRepository.checkExistsByName(newTypeName)).isTrue();

        var allMeterTypeModels = meterTypeRepository.getAll();
        var newTypeModelOptional =
                allMeterTypeModels.stream().filter(m -> m.typeName().equals(newTypeName)).findFirst();
        assertThat(newTypeModelOptional).isPresent();
    }

    @Test
    void testGetAll_whenSave_thenReturnCorrectData() {
        var meterType1 = MeterType.builder().typeName("Water").build();
        var meterType2 = MeterType.builder().typeName("Gas").build();

        var allMeterTypeModelsSizeBeforeSave = meterTypeRepository.getAll().size();

        meterTypeRepository.save(Set.of(meterType1, meterType2));

        var allMeterTypeModelsAfterSave = meterTypeRepository.getAll();
        assertThat(allMeterTypeModelsAfterSave)
                .hasSize(allMeterTypeModelsSizeBeforeSave + 2)
                .anyMatch(m -> m.typeName().equals("Water"))
                .anyMatch(m -> m.typeName().equals("Gas"));
    }

    @Test
    void testSave_whenCorrectData_thenExistsInDatabase() {
        var newTypeName = "Air";
        var meterType = MeterType.builder().typeName(newTypeName).build();
        assertThat(meterTypeRepository.checkExistsByName(newTypeName)).isFalse();

        meterTypeRepository.save(meterType);
        assertThat(meterTypeRepository.checkExistsByName(newTypeName)).isTrue();
    }

    @Test
    void testSaveAll_whenCorrectData_thenExistsInDatabase() {
        var meterType1 = MeterType.builder().typeName("Petrol").build();
        var meterType2 = MeterType.builder().typeName("Diesel").build();

        var allMeterTypeModelsSizeBeforeSave = meterTypeRepository.getAll().size();

        meterTypeRepository.save(Set.of(meterType1, meterType2));

        var allMeterTypeModelsAfterSave = meterTypeRepository.getAll();

        assertThat(allMeterTypeModelsAfterSave)
                .hasSize(allMeterTypeModelsSizeBeforeSave + 2);
    }
}