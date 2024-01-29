package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.dto.AuditionEventDTO;
import ru.ylab.mapper.AuditionEventMapper;
import ru.ylab.repository.AuditionEventRepository;

import java.util.Collection;

/**
 * The AuditionEventService class provides functionality for managing audition events.
 * It includes methods for retrieving events by user ID and adding new audition events.
 * This service interacts with the AuditionEventRepository.
 */
@RequiredArgsConstructor
public class AuditionEventService {
    private final AuditionEventRepository auditionEventRepository;

    /**
     * Retrieves a collection of audition events associated with a specific user.
     *
     * @param userId The ID of the user for whom audition events are retrieved.
     * @return Collection of AuditionEventDTO representing the user's audition events.
     */
    public Collection<AuditionEventDTO> getEventsByUserId(Long userId) {
        return AuditionEventMapper.MAPPER.toAuditionEventDTOs(
                auditionEventRepository.getEventsByUserId(userId)
        );
    }

    /**
     * Adds a new audition event to the repository.
     *
     * @param auditionEvent The AuditionEvent to be added.
     */
    public void addEvent(AuditionEvent auditionEvent) {
        auditionEventRepository.addEvent(auditionEvent);
    }
}
