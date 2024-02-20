package io.ylab.service;

import io.ylab.annotation.Auditable;
import io.ylab.dto.request.NewMeterTypeRequestDto;
import io.ylab.dto.response.MessageDto;
import io.ylab.entity.MeterType;
import io.ylab.enumerated.AuditionEventType;
import io.ylab.exception.MeterTypeExistException;
import io.ylab.exception.MeterTypeNotFoundException;
import io.ylab.mapper.MeterTypeMapper;
import io.ylab.repository.MeterTypeRepository;
import io.ylab.utils.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * The MeterTypeService class provides functionality related to meter types.
 * It includes methods for adding new meter types, retrieving existing types, and checking type existence.
 * This service interacts with the MeterTypeRepository and other services as needed.
 */
@Service
@RequiredArgsConstructor
public class MeterTypeService {
    private final MeterTypeRepository meterTypeRepository;
    private final MeterTypeMapper meterTypeMapper;

    /**
     * Saves a new meter type with the provided type name.
     *
     * @param requestDto Contains the name of the new meter type to be added.
     * @throws MeterTypeExistException If a meter type with the same name already exists.
     */
    @Transactional
    @Auditable(eventType = AuditionEventType.NEW_METER_TYPE_ADDITION)
    public MessageDto save(NewMeterTypeRequestDto requestDto) {
        var typeName = requestDto.typeName();
        if (meterTypeRepository.checkExistsByName(typeName)) {
            throw new MeterTypeExistException(typeName);
        }
        var newType = MeterType.builder().typeName(typeName).build();
        meterTypeRepository.save(newType);
        return ResponseUtils.responseWithMessage("Meter type '%s' saved".formatted(typeName));
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
        return meterTypeMapper.toMeterType(meterTypeModel);
    }

    /**
     * Retrieves all existing meter types.
     *
     * @return Collection of MeterType representing all existing meter types.
     */
    public Collection<MeterType> getAll() {
        return meterTypeMapper.toMeterTypes(meterTypeRepository.getAll());
    }
}