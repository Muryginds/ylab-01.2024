package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.dto.request.NewMeterTypeRequestDTO;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.entity.MeterType;
import ru.ylab.enumerated.AuditionEventType;
import ru.ylab.exception.MeterTypeExistException;
import ru.ylab.exception.MeterTypeNotFoundException;
import ru.ylab.dto.response.MeterTypeDTO;
import ru.ylab.mapper.MeterTypeMapper;
import ru.ylab.repository.MeterTypeRepository;

import java.util.Collection;

/**
 * The MeterTypeService class provides functionality related to meter types.
 * It includes methods for adding new meter types, retrieving existing types, and checking type existence.
 * This service interacts with the MeterTypeRepository and other services as needed.
 */
@RequiredArgsConstructor
public class MeterTypeService {
    private final MeterTypeRepository meterTypeRepository;
    private final AuditionEventService auditionEventService;
    private final UserService userService;

    /**
     * Saves a new meter type with the provided type name.
     * Audits the action and logs an audition event.
     *
     * @param request Contains the name of the new meter type to be added.
     * @throws MeterTypeExistException If a meter type with the same name already exists.
     */
    public void save(NewMeterTypeRequestDTO request) {
        var typeName = request.typeName();
        if (meterTypeRepository.checkExistsByName(typeName)) {
            throw new MeterTypeExistException(typeName);
        }
        var newType = MeterType.builder().typeName(typeName).build();
        meterTypeRepository.save(newType);
        newMeterTypeAdditionAuditionEvent(typeName);
    }

    private void newMeterTypeAdditionAuditionEvent(String typeName) {
        var event = AuditionEvent.builder()
                .user(userService.getCurrentUser())
                .eventType(AuditionEventType.NEW_METER_TYPE_ADDITION)
                .message(String.format(
                        "New meter type '%s' added",
                        typeName))
                .build();
        auditionEventService.save(event);
    }

    /**
     * Retrieves a meter type by its ID and converts it to a DTO.
     *
     * @param meterTypeId The ID of the meter type to retrieve.
     * @return MeterTypeDTO representing the retrieved meter type.
     * @throws MeterTypeNotFoundException If no meter type is found with the given ID.
     */
    public MeterTypeDTO getMeterTypeDTOById(Long meterTypeId) {
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
