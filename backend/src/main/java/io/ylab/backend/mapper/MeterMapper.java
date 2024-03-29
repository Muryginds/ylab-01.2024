package io.ylab.backend.mapper;

import io.ylab.commons.entity.Meter;
import io.ylab.commons.entity.MeterType;
import io.ylab.commons.entity.User;
import io.ylab.backend.dto.response.MeterDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import io.ylab.backend.model.MeterModel;

import java.util.Collection;

@Mapper(componentModel = "spring", uses = {UserMapper.class, MeterTypeMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MeterMapper {

    @Mapping(target = "userDTO", source = "user")
    @Mapping(target = "meterTypeDTO", source = "meterType")
    MeterDto toMeterDTO(Meter meter);

    Collection<MeterDto> toMeterDTOs(Collection<Meter> meters);

    @Mapping(target = "id", source = "meterModel.id")
    Meter toMeter(MeterModel meterModel, MeterType meterType, User user);

    @Mapping(target = "userId", source = "meter.user.id")
    @Mapping(target = "meterTypeId", source = "meter.meterType.id")
    MeterModel toMeterModel(Meter meter);
}
