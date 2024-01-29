package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.dto.AuditionEventDTO;

import java.util.Collection;

@Mapper(uses = {UserMapper.class})
public interface AuditionEventMapper {

    AuditionEventMapper MAPPER = Mappers.getMapper(AuditionEventMapper.class);

    @Mapping(target = "userDTO", source = "user")
    AuditionEventDTO toAuditionEventDTO(AuditionEvent auditionEvent);

    Collection<AuditionEventDTO> toAuditionEventDTOs(Collection<AuditionEvent> auditionEvents);
}
