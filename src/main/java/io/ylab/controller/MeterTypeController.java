package io.ylab.controller;

import lombok.RequiredArgsConstructor;
import io.ylab.dto.request.NewMeterTypeRequestDTO;
import io.ylab.exception.MeterTypeExistException;
import io.ylab.service.MeterTypeService;

/**
 * Controller class for handling meter type-related operations and interactions.
 *
 * <p>This class acts as an interface between the user interface and the underlying business logic
 * related to meter types.
 */
@RequiredArgsConstructor
public class MeterTypeController {
    /**
     * The associated service for meter type-related operations.
     */
    private final MeterTypeService meterTypeService;

    /**
     * Checks if a meter type with the specified name already exists.
     *
     * @param typeName The name of the meter type to check.
     * @return `true` if the meter type exists, otherwise `false`.
     */
    public boolean checkExistsByName(String typeName) {
        return meterTypeService.checkExistsByName(typeName);
    }

    /**
     * Saves a new meter type with the specified name.
     *
     * @param request Contains the name of the new meter type to be saved.
     * @throws MeterTypeExistException If a meter type with the same name already exists.
     */
    public void save(NewMeterTypeRequestDTO request) {
        meterTypeService.save(request);
    }
}
