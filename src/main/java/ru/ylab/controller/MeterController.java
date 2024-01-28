package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.in.dto.MeterDTO;
import ru.ylab.service.MeterService;

import java.util.Collection;

@RequiredArgsConstructor
public class MeterController {
    private final MeterService meterService;

    public Collection<MeterDTO> getAllByUserId(Long userId) {
        return meterService.getMeterDTOsByUserId(userId);
    }
}
