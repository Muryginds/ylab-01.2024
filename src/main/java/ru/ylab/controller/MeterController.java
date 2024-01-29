package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.in.dto.MeterDTO;
import ru.ylab.service.MeterService;

import java.util.Collection;

/**
 * Controller class for handling meter-related operations and interactions.
 *
 * <p>This class acts as an interface between the user interface and the underlying business logic
 * related to meters.
 */
@RequiredArgsConstructor
public class MeterController {
    /**
     * The associated service for meter-related operations.
     */
    private final MeterService meterService;

    /**
     * Retrieves all meters associated with a specific user.
     *
     * @param userId The ID of the user for which to retrieve meters.
     * @return A collection of meter DTOs associated with the specified user.
     */
    public Collection<MeterDTO> getAllByUserId(Long userId) {
        return meterService.getMeterDTOsByUserId(userId);
    }
}
