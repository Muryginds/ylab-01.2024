package io.ylab.controller;

import io.ylab.dto.response.AuditionEventDto;
import lombok.RequiredArgsConstructor;
import io.ylab.dto.request.AuditionEventsRequestDto;
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
     * @param requestDto Contains the ID of the user.
     * @return A collection of AuditionEventDTO representing the audition events.
     */
    public Collection<AuditionEventDto> getEvents(AuditionEventsRequestDto requestDto){
        return auditionEventService.getEvents(requestDto);
    }
}
