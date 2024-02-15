package io.ylab.service;

import lombok.RequiredArgsConstructor;
import io.ylab.annotation.Auditable;
import io.ylab.dto.request.NewMeterTypeRequestDto;
import io.ylab.dto.response.MeterTypeDto;
import io.ylab.entity.MeterType;
import io.ylab.enumerated.AuditionEventType;
import io.ylab.exception.MeterTypeExistException;
import io.ylab.exception.MeterTypeNotFoundException;
import io.ylab.mapper.MeterTypeMapper;
import io.ylab.repository.MeterTypeRepository;

import java.util.Collection;

/**
 * The MeterTypeService class provides functionality related to meter types.
 * It includes methods for adding new meter types, retrieving existing types, and checking type existence.
 * This service interacts with the MeterTypeRepository and other services as needed.
 */
@RequiredArgsConstructor
public class MeterTypeService {
    private final MeterTypeRepository meterTypeRepository;

    /**
     * Saves a new meter type with the provided type name.
     *
     * @param requestDto Contains the name of the new meter type to be added.
     * @throws MeterTypeExistException If a meter type with the same name already exists.
     */
    @Auditable(eventType = AuditionEventType.NEW_METER_TYPE_ADDITION)
    public void save(NewMeterTypeRequestDto requestDto) {
        var typeName = requestDto.typeName();
        if (meterTypeRepository.checkExistsByName(typeName)) {
            throw new MeterTypeExistException(typeName);
        }
        var newType = MeterType.builder().typeName(typeName).build();
        meterTypeRepository.save(newType);
    }

    /**
     * Retrieves a meter type by its ID and converts it to a DTO.
     *
     * @param meterTypeId The ID of the meter type to retrieve.
     * @return MeterTypeDTO representing the retrieved meter type.
     * @throws MeterTypeNotFoundException If no meter type is found with the given ID.
     */
    public MeterTypeDto getMeterTypeDTOById(Long meterTypeId) {
        var meterType = getMeterTypeById(meterTypeId);
        return MeterTypeMapper.MAPPER.toMeterTypeDTO(meterType);
    }

    /**
     * Retrieves a meter type by its ID.
     *
     * @param meterTypeId The ID of the meter type to retrieve.
     * @return MeterType entity representing the retrieved meter type.
     * @throws MeterTypeNotFoundException If no meter type is found with the given ID.
     */
    public MeterType getMeterTypeById(Long meterTypeId) {
        var meterTypeModel = meterTypeRepository.findById(meterTypeId)
                .orElseThrow(() -> new MeterTypeNotFoundException(meterTypeId));
        return MeterTypeMapper.MAPPER.toMeterType(meterTypeModel);
    }

    /**
     * Retrieves all existing meter types.
     *
     * @return Collection of MeterType representing all existing meter types.
     */
    public Collection<MeterType> getAll() {
        return MeterTypeMapper.MAPPER.toMeterTypes(meterTypeRepository.getAll());
    }

    /**
     * Checks if a meter type with the given name already exists.
     *
     * @param typeName The name of the meter type to check.
     * @return true if a meter type with the given name exists, false otherwise.
     */
    public boolean checkExistsByName(String typeName) {
        return meterTypeRepository.checkExistsByName(typeName);
    }
}
