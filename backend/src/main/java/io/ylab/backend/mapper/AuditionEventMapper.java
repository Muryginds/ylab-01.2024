package io.ylab.backend.mapper;

import io.ylab.commons.entity.AuditionEvent;
import io.ylab.commons.entity.User;
import io.ylab.backend.dto.response.AuditionEventDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import io.ylab.backend.model.AuditionEventModel;

import java.util.Set;

@Mapper(componentModel = "spring", uses = {UserMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface AuditionEventMapper {

    @Mapping(target = "userDTO", source = "user")
    AuditionEventDto toAuditionEventDTO(AuditionEvent auditionEvent);

    Set<AuditionEventDto> toAuditionEventDTOs(Set<AuditionEvent> auditionEvents);

    @Mapping(target = "id", source = "auditionEventModel.id")
    AuditionEvent toAuditionEvent(AuditionEventModel auditionEventModel, User user);

    @Mapping(target = "userId", source = "auditionEvent.user.id")
    AuditionEventModel toAuditionEventModel(AuditionEvent auditionEvent);
}
