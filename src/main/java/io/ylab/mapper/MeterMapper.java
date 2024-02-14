package io.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import io.ylab.entity.Meter;
import io.ylab.dto.response.MeterDTO;
import io.ylab.entity.MeterType;
import io.ylab.entity.User;
import io.ylab.model.MeterModel;

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
