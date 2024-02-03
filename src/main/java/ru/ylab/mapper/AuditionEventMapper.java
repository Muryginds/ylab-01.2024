package ru.ylab.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.dto.AuditionEventDTO;
import ru.ylab.entity.User;
import ru.ylab.model.AuditionEventModel;

import java.util.Collection;

@Mapper(uses = {UserMapper.class})
public interface AuditionEventMapper {

    AuditionEventMapper MAPPER = Mappers.getMapper(AuditionEventMapper.class);

    @Mapping(target = "userDTO", source = "user")
    AuditionEventDTO toAuditionEventDTO(AuditionEvent auditionEvent);

    Collection<AuditionEventDTO> toAuditionEventDTOs(Collection<AuditionEvent> auditionEvents);

    @Mapping(target = "id", source = "auditionEventModel.id")
    AuditionEvent toAuditionEvent(AuditionEventModel auditionEventModel, User user);

    @Mapping(target = "userId", source = "auditionEvent.user.id")
    AuditionEventModel toAuditionEventModel(AuditionEvent auditionEvent);
}
