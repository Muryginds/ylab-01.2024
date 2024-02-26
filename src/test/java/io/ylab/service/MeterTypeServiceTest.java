package io.ylab.service;

import io.ylab.dto.request.NewMeterTypeRequestDto;
import io.ylab.dto.response.MessageDto;
import io.ylab.entity.MeterType;
import io.ylab.exception.MeterTypeExistException;
import io.ylab.mapper.MeterTypeMapper;
import io.ylab.repository.MeterTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        NewMeterTypeRequestDto requestDto = new NewMeterTypeRequestDto("Electricity");
        MeterType newType = MeterType.builder().typeName("Electricity").build();

        Mockito.when(meterTypeRepository.checkExistsByName("Electricity")).thenReturn(false);

        MessageDto result = meterTypeService.save(requestDto);

        assertEquals("Meter type 'Electricity' saved", result.message());
        Mockito.verify(meterTypeRepository, Mockito.times(1)).save(newType);
    }

    @Test
    void saveNewMeterType_WhenTypeExists_ShouldThrowException() {
        NewMeterTypeRequestDto requestDto = new NewMeterTypeRequestDto("Electricity");

        Mockito.when(meterTypeRepository.checkExistsByName("Electricity")).thenReturn(true);

        assertThrows(MeterTypeExistException.class, () -> meterTypeService.save(requestDto));
    }
}