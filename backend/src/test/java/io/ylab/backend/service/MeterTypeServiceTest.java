package io.ylab.backend.service;

import io.ylab.backend.dto.request.NewMeterTypeRequestDto;
import io.ylab.commons.entity.MeterType;
import io.ylab.backend.exception.MeterTypeExistException;
import io.ylab.backend.mapper.MeterTypeMapper;
import io.ylab.backend.repository.MeterTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeterTypeServiceTest {
    @Mock
    private MeterTypeRepository meterTypeRepository;
    @Mock
    private MeterTypeMapper meterTypeMapper;
    @InjectMocks
    private MeterTypeService meterTypeService;

    @Test
    void saveNewMeterType_WhenTypeDoesNotExist_ShouldSaveSuccessfully() {
        var requestDto = new NewMeterTypeRequestDto("Electricity");
        var newType = MeterType.builder().typeName("Electricity").build();

        when(meterTypeRepository.checkExistsByName("Electricity")).thenReturn(false);

        var result = meterTypeService.save(requestDto);

        assertEquals("Meter type 'Electricity' saved", result.message());
        Mockito.verify(meterTypeRepository, Mockito.times(1)).save(newType);
    }

    @Test
    void saveNewMeterType_WhenTypeExists_ShouldThrowException() {
        var requestDto = new NewMeterTypeRequestDto("Electricity");

        when(meterTypeRepository.checkExistsByName("Electricity")).thenReturn(true);

        assertThrows(MeterTypeExistException.class, () -> meterTypeService.save(requestDto));
    }
}