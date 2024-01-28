package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.entity.MeterType;
import ru.ylab.enumerated.AuditionEventType;
import ru.ylab.exception.MeterTypeExistException;
import ru.ylab.exception.MeterTypeNotFoundException;
import ru.ylab.in.dto.MeterTypeDTO;
import ru.ylab.mapper.MeterTypeMapper;
import ru.ylab.repository.MeterTypeRepository;

import java.util.Collection;

@RequiredArgsConstructor
public class MeterTypeService {
    private final MeterTypeRepository meterTypeRepository;
    private final AuditionEventService auditionEventService;
    private final UserService userService;

    public void save(String typeName) {
        if (meterTypeRepository.checkExistsByName(typeName)) {
            throw new MeterTypeExistException(typeName);
        }
        var newType = MeterType.builder().typeName(typeName).build();
        meterTypeRepository.save(newType);
        var event = AuditionEvent.builder()
                .user(userService.getCurrentUser())
                .type(AuditionEventType.NEW_METER_TYPE_ADDITION)
                .message(String.format(
                        "New meter type '%s' added",
                        typeName))
                .build();
        auditionEventService.addEvent(event);
    }

    public MeterTypeDTO getById(Long meterTypeId) {
        var meterType = meterTypeRepository.findById(meterTypeId)
                .orElseThrow(() -> new MeterTypeNotFoundException(meterTypeId));
        return MeterTypeMapper.MAPPER.toMeterTypeDTO(meterType);
    }

    public Collection<MeterType> getAll() {
        return meterTypeRepository.getAll();
    }

    public boolean checkExistsByName(String typeName) {
        return meterTypeRepository.checkExistsByName(typeName);
    }
}
