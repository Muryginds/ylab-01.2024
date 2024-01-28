package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.service.MeterTypeService;

@RequiredArgsConstructor
public class MeterTypeController {
    private final MeterTypeService meterTypeService;

    public boolean checkExistsByName(String typeName) {
        return meterTypeService.checkExistsByName(typeName);
    }

    public void save(String typeName) {
        meterTypeService.save(typeName);
    }
}
