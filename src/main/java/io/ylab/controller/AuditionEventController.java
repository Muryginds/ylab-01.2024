package io.ylab.controller;

import lombok.RequiredArgsConstructor;
import io.ylab.dto.request.AuditionEventsRequestDTO;
import io.ylab.dto.response.AuditionEventDTO;
import io.ylab.service.AuditionEventService;

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
     * @param request Contains the ID of the user.
     * @return A collection of AuditionEventDTO representing the audition events.
     */
    public Collection<AuditionEventDTO> getEvents(AuditionEventsRequestDTO request){
        return auditionEventService.getEvents(request);
    }
}
