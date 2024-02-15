package io.ylab.mapper;

import io.ylab.dto.response.MeterTypeDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import io.ylab.entity.MeterType;
import io.ylab.model.MeterTypeModel;

import java.util.Collection;
import java.util.List;

@Mapper
public interface MeterTypeMapper {
    MeterTypeMapper MAPPER = Mappers.getMapper(MeterTypeMapper.class);

    MeterTypeDto toMeterTypeDTO(MeterType meterType);

    MeterType toMeterType(MeterTypeModel meterTypeModel);

    List<MeterType> toMeterTypes(Collection<MeterTypeModel> meterTypeModels);
}
