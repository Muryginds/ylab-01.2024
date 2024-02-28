package io.ylab.backend.mapper;

import io.ylab.commons.entity.MeterType;
import io.ylab.backend.dto.response.MeterTypeDto;
import org.mapstruct.Mapper;
import io.ylab.backend.model.MeterTypeModel;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MeterTypeMapper {

    MeterTypeDto toMeterTypeDto(MeterType meterType);

    MeterType toMeterType(MeterTypeModel meterTypeModel);

    List<MeterType> toMeterTypes(Collection<MeterTypeModel> meterTypeModels);
}
