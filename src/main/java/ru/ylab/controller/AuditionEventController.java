package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.dto.AuditionEventDTO;
import ru.ylab.service.AuditionEventService;

import java.util.Collection;

/**
 * Controller responsible for handling audition event-related operations.
 */
@RequiredArgsConstructor
public class AuditionEventController {
    private final AuditionEventService auditionEventService;

    /**
     * Retrieves audition events associated with a specific user ID.
     *
     * @param userId The ID of the user.
     * @return A collection of AuditionEventDTO representing the audition events.
     */
    public Collection<AuditionEventDTO> getEventsByUserId(Long userId){
        return auditionEventService.getEventsByUserId(userId);
    }
}
