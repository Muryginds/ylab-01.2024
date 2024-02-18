package io.ylab.service;

import io.ylab.dto.response.AuditionEventDto;
import io.ylab.entity.AuditionEvent;
import io.ylab.enumerated.UserRole;
import io.ylab.exception.NoPermissionException;
import io.ylab.mapper.AuditionEventMapper;
import io.ylab.repository.AuditionEventRepository;
import io.ylab.utils.CurrentUserUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

/**
 * The AuditionEventServiceImpl class provides functionality for managing audition events.
 * It includes methods for retrieving events by user ID and adding new audition events.
 * This service interacts with the AuditionEventRepository.
 */
@Service
@RequiredArgsConstructor
public class AuditionEventService {
    private final AuditionEventRepository auditionEventRepository;
    private final UserService userService;
    private final AuditionEventMapper auditionEventMapper;

    /**
     * Retrieves a collection of audition events associated with a specific user.
     *
     * @param userId Contains the ID of the user for whom audition events are retrieved.
     * @return Collection of AuditionEventDTO representing the user's audition events.
     */
    public Set<AuditionEventDto> getEvents(long userId) {
        var currentUser = CurrentUserUtils.getCurrentUser();
        if (currentUser == null || !currentUser.getRole().equals(UserRole.ADMIN)) {
            throw new NoPermissionException();
        }
        var eventModels = auditionEventRepository.getEventsByUserId(userId);
        var collection = new HashSet<AuditionEvent>();
        if (!eventModels.isEmpty()) {
            var targetUser = userService.getUserById(userId);
            for (var eventModel : eventModels) {
                collection.add(auditionEventMapper.toAuditionEvent(eventModel, targetUser));
            }
        }
        return auditionEventMapper.toAuditionEventDTOs(collection);
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
