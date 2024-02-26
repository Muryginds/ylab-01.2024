package io.ylab.mapper;

import io.ylab.dto.response.MeterTypeDto;
import org.mapstruct.Mapper;
import io.ylab.entity.MeterType;
import io.ylab.model.MeterTypeModel;

import java.util.Collection;
import java.util.List;

@Mapper(componentModel = "spring")
public interface MeterTypeMapper {

    MeterTypeDto toMeterTypeDto(MeterType meterType);

    MeterType toMeterType(MeterTypeModel meterTypeModel);

    List<MeterType> toMeterTypes(Collection<MeterTypeModel> meterTypeModels);
}
