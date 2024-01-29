package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.ylab.entity.Meter;
import ru.ylab.in.dto.MeterDTO;

import java.util.Collection;

@Mapper(uses = {UserMapper.class, MeterTypeMapper.class})
public interface MeterMapper {
    MeterMapper MAPPER = Mappers.getMapper(MeterMapper.class);

    @Mapping(target = "userDTO", source = "user")
    @Mapping(target = "typeDTO", source = "type")
    MeterDTO toMeterDTO(Meter meter);

    Collection<MeterDTO> toMeterDTOs(Collection<Meter> meters);
}
