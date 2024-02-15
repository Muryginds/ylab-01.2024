package io.ylab.mapper;

import io.ylab.dto.response.AuditionEventDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import io.ylab.entity.AuditionEvent;
import io.ylab.entity.User;
import io.ylab.model.AuditionEventModel;

import java.util.Collection;

@Mapper(uses = {UserMapper.class})
public interface AuditionEventMapper {

    AuditionEventMapper MAPPER = Mappers.getMapper(AuditionEventMapper.class);

    @Mapping(target = "userDTO", source = "user")
    AuditionEventDto toAuditionEventDTO(AuditionEvent auditionEvent);

    Collection<AuditionEventDto> toAuditionEventDTOs(Collection<AuditionEvent> auditionEvents);

    @Mapping(target = "id", source = "auditionEventModel.id")
    AuditionEvent toAuditionEvent(AuditionEventModel auditionEventModel, User user);

    @Mapping(target = "userId", source = "auditionEvent.user.id")
    AuditionEventModel toAuditionEventModel(AuditionEvent auditionEvent);
}
