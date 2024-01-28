package ru.ylab.controller;

import lombok.RequiredArgsConstructor;
import ru.ylab.in.dto.AuditionEventDTO;
import ru.ylab.service.AuditionEventService;

import java.util.Collection;

@RequiredArgsConstructor
public class AuditionEventController {
    private final AuditionEventService auditionEventService;

    public Collection<AuditionEventDTO> getBySubmissionId(Long userId){
        return auditionEventService.getEventsByUserId(userId);
    }
}
