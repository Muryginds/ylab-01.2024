package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.ylab.entity.MeterType;
import ru.ylab.dto.MeterTypeDTO;

@Mapper
public interface MeterTypeMapper {
    MeterTypeMapper MAPPER = Mappers.getMapper(MeterTypeMapper.class);

    MeterTypeDTO toMeterTypeDTO(MeterType meterType);
}
