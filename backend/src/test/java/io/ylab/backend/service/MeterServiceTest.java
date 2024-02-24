package io.ylab.backend.service;

import io.ylab.commons.entity.Meter;
import io.ylab.commons.entity.User;
import io.ylab.backend.exception.MeterNotFoundException;
import io.ylab.backend.mapper.MeterMapper;
import io.ylab.backend.model.MeterModel;
import io.ylab.backend.repository.MeterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MeterServiceTest {
    @Mock
    private MeterRepository meterRepository;
    @Mock
    private MeterTypeService meterTypeService;
    @Mock
    private UserService userService;
    @Mock
    private MeterMapper meterMapper;
    @InjectMocks
    private MeterService meterService;

    @Test
    void testSaveMeter() {
        var meter = Meter.builder().build();

        meterService.save(meter);

        verify(meterRepository, times(1)).save(meter);
    }

    @Test
    void testSaveMeters() {
        var meters = Collections.singleton(Meter.builder().build());

        meterService.save(meters);

        verify(meterRepository, times(1)).save(meters);
    }

    @Test
    void testGetMeterById_NotFound() {
        var meterId = 1L;
        when(meterRepository.findById(meterId)).thenReturn(Optional.empty());

        assertThrows(MeterNotFoundException.class, () -> meterService.getById(meterId));
    }

    @Test
    void testGetMetersByUserId() {
        var userId = 1L;
        var user = User.builder().id(userId).build();
        var meterModel = MeterModel.builder().build();
        when(userService.getUserById(userId)).thenReturn(user);
        when(meterRepository.getByUserId(userId)).thenReturn(Collections.singleton(meterModel));

        Collection<Meter> result = meterService.getMetersByUserId(userId);

        assertEquals(1, result.size());
    }

    @Test
    void testGenerateForNewUser() {
        var user = User.builder().build();
        when(meterTypeService.getAll()).thenReturn(Collections.emptyList());

        meterService.generateForNewUser(user);

        verify(meterRepository, times(1)).save(anySet());
    }
}
