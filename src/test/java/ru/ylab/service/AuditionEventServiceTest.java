package ru.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import ru.ylab.entity.AuditionEvent;
import ru.ylab.entity.User;
import ru.ylab.enumerated.AuditionEventType;
import ru.ylab.dto.AuditionEventDTO;
import ru.ylab.mapper.AuditionEventMapper;
import ru.ylab.repository.AuditionEventRepository;

import java.util.Collection;
import java.util.Set;

class AuditionEventServiceTest {

    @Mock
    private AuditionEventRepository auditionEventRepository;

    @InjectMocks
    private AuditionEventService auditionEventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEventsByUserId_whenExistingUser_thenReturnCollectionOfAuditionEventDTOs() {
        long userId = 1L;
        User user = User.builder().build();
        Set<AuditionEvent> auditionEvents = Set.of(
                AuditionEvent.builder().id(1L).user(user).type(AuditionEventType.SESSION_START).message("User logged in").build(),
                AuditionEvent.builder().id(2L).user(user).type(AuditionEventType.SESSION_END).message("User logged out").build()
        );
        Mockito.when(auditionEventRepository.getEventsByUserId(userId)).thenReturn(auditionEvents);

        Collection<AuditionEventDTO> result = auditionEventService.getEventsByUserId(userId);

        Collection<AuditionEventDTO> expected = AuditionEventMapper.MAPPER.toAuditionEventDTOs(auditionEvents);
        Assertions.assertEquals(expected, result);
    }

    @Test
    void testAddEvent_whenValid_thenDoNothing() {
        AuditionEvent auditionEvent = AuditionEvent.builder()
                .id(1L)
                .user(User.builder().build())
                .type(AuditionEventType.REGISTRATION)
                .message("User logged in")
                .build();

        auditionEventService.addEvent(auditionEvent);

        Mockito.verify(auditionEventRepository, Mockito.times(1)).addEvent(auditionEvent);
    }
}
