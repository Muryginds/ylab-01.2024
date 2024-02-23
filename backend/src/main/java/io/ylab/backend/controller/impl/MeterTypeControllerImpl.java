package io.ylab.backend.controller.impl;

import io.ylab.backend.dto.request.NewMeterTypeRequestDto;
import io.ylab.backend.exception.MeterTypeExistException;
import io.ylab.backend.service.MeterTypeService;
import io.ylab.backend.controller.MeterTypeController;
import io.ylab.backend.dto.response.MessageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Implementation of controller class for handling meter type-related operations and interactions.
 *
 * <p>This class acts as an interface between the user interface and the underlying business logic
 * related to meter types.
 */
@RestController
@RequestMapping("/api/v1/meter-types")
@RequiredArgsConstructor
public class MeterTypeControllerImpl implements MeterTypeController {
    private final MeterTypeService meterTypeService;

    /**
     * Saves a new meter type with the specified name.
     *
     * @param requestDto Contains the name of the new meter type to be saved.
     * @throws MeterTypeExistException If a meter type with the same name already exists.
     */
    @Override
    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    public MessageDto save(@Valid @RequestBody NewMeterTypeRequestDto requestDto) {
        return meterTypeService.save(requestDto);
    }
}
