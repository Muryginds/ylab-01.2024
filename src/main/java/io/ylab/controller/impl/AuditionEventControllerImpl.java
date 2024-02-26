package io.ylab.controller.impl;

import io.ylab.controller.AuditionEventController;
import io.ylab.dto.response.AuditionEventDto;
import io.ylab.service.AuditionEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 * Controller responsible for handling audition event-related operations.
 */
@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class AuditionEventControllerImpl implements AuditionEventController {
    private final AuditionEventService auditionEventService;

    /**
     * Retrieves audition events associated with a specific user ID.
     *
     * @param userId Contains the ID of the user.
     * @return A collection of AuditionEventDTO representing the audition events.
     */
    @Override
    @GetMapping(value = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<AuditionEventDto> getEvents(@RequestParam(name = "userId") long userId) {
        return auditionEventService.getEvents(userId);
    }
}
