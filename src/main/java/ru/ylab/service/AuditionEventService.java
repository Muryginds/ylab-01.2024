package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.in.dto.AuditionEventDTO;
import ru.ylab.mapper.AuditionEventMapper;
import ru.ylab.repository.AuditionEventRepository;

import java.util.Collection;

@RequiredArgsConstructor
public class AuditionEventService {
    private final AuditionEventRepository auditionEventRepository;

    public Collection<AuditionEventDTO> getEventsByUserId(Long userId) {
        return AuditionEventMapper.MAPPER.toAuditionEventDTOs(
                auditionEventRepository.getEventsByUserId(userId)
        );
    }

    public void addEvent(AuditionEvent auditionEvent) {
        auditionEventRepository.addEvent(auditionEvent);
    }
}
