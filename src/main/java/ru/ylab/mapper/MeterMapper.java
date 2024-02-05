package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.ylab.entity.Meter;
import ru.ylab.dto.MeterDTO;
import ru.ylab.entity.MeterType;
import ru.ylab.entity.User;
import ru.ylab.model.MeterModel;

import java.util.Collection;

@Mapper(uses = {UserMapper.class, MeterTypeMapper.class})
public interface MeterMapper {
    MeterMapper MAPPER = Mappers.getMapper(MeterMapper.class);

    @Mapping(target = "userDTO", source = "user")
    @Mapping(target = "meterTypeDTO", source = "meterType")
    MeterDTO toMeterDTO(Meter meter);

    Collection<MeterDTO> toMeterDTOs(Collection<Meter> meters);

    @Mapping(target = "id", source = "meterModel.id")
    Meter toMeter(MeterModel meterModel, MeterType meterType, User user);

    @Mapping(target = "userId", source = "meter.user.id")
    @Mapping(target = "meterTypeId", source = "meter.meterType.id")
    MeterModel toMeterModel(Meter meter);
}
