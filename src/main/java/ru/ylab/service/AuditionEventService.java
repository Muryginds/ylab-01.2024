package ru.ylab.service;

import lombok.RequiredArgsConstructor;
import ru.ylab.dto.request.AuditionEventsRequestDTO;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.dto.response.AuditionEventDTO;
import ru.ylab.enumerated.UserRole;
import ru.ylab.exception.NoPermissionException;
import ru.ylab.mapper.AuditionEventMapper;
import ru.ylab.repository.AuditionEventRepository;

import java.util.Collection;
import java.util.HashSet;

/**
 * The AuditionEventService class provides functionality for managing audition events.
 * It includes methods for retrieving events by user ID and adding new audition events.
 * This service interacts with the AuditionEventRepository.
 */
@RequiredArgsConstructor
public class AuditionEventService {
    private final AuditionEventRepository auditionEventRepository;
    private final UserService userService;

    /**
     * Retrieves a collection of audition events associated with a specific user.
     *
     * @param request Contains the ID of the user for whom audition events are retrieved.
     * @return Collection of AuditionEventDTO representing the user's audition events.
     */
    public Collection<AuditionEventDTO> getEvents(AuditionEventsRequestDTO request) {
        var userId = request.userId();
        var currentUser = userService.getCurrentUser();
        if (currentUser == null || !currentUser.getRole().equals(UserRole.ADMIN)) {
            throw new NoPermissionException();
        }
        var eventModels = auditionEventRepository.getEventsByUserId(userId);
        var collection = new HashSet<AuditionEvent>();
        if (!eventModels.isEmpty()) {
            var targetUser = userService.getUserById(userId);
            for (var eventModel : eventModels) {
                collection.add(AuditionEventMapper.MAPPER.toAuditionEvent(eventModel, targetUser));
            }
        }
        return AuditionEventMapper.MAPPER.toAuditionEventDTOs(collection);
    }

    /**
     * Saves a new audition event to the repository.
     *
     * @param auditionEvent The AuditionEvent to be saved.
     */
    public void save(AuditionEvent auditionEvent) {
        auditionEventRepository.save(auditionEvent);
    }
}
