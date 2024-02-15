package io.ylab.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import io.ylab.dto.request.AuditionEventsRequestDto;
import io.ylab.entity.AuditionEvent;
import io.ylab.entity.User;
import io.ylab.enumerated.AuditionEventType;
import io.ylab.enumerated.UserRole;
import io.ylab.mapper.AuditionEventMapper;
import io.ylab.repository.AuditionEventRepository;

import java.util.Set;

class AuditionEventServiceTest {

    @Mock
    private AuditionEventRepository auditionEventRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private AuditionEventService auditionEventService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetEventsByUserId_whenExistingUser_thenReturnCollectionOfAuditionEventDTOs() {
        var userId = 1L;
        var request = AuditionEventsRequestDto.builder().userId(userId).build();
        var adminUser = User.builder().id(userId).role(UserRole.ADMIN).build();
        var event1 =  AuditionEvent.builder().id(1L).user(adminUser)
                .eventType(AuditionEventType.SESSION_START).message("User logged in").build();
        var event2 = AuditionEvent.builder().id(2L).user(adminUser)
                .eventType(AuditionEventType.SESSION_END).message("User logged out").build();
        var auditionEventModels = Set.of(
                AuditionEventMapper.MAPPER.toAuditionEventModel(event1),
                AuditionEventMapper.MAPPER.toAuditionEventModel(event2)
        );
        var expectedEventDTOs = Set.of(
                AuditionEventMapper.MAPPER.toAuditionEventDTO(event1),
                AuditionEventMapper.MAPPER.toAuditionEventDTO(event2)
        );
        Mockito.when(auditionEventRepository.getEventsByUserId(userId)).thenReturn(auditionEventModels);
        Mockito.when(userService.getUserById(userId)).thenReturn(adminUser);
        Mockito.when(userService.getCurrentUser()).thenReturn(adminUser);

        var result = auditionEventService.getEvents(request);

        Assertions.assertTrue(result.containsAll(expectedEventDTOs));
        Assertions.assertEquals(result.size(), expectedEventDTOs.size());
    }

    @Test
    void testSave_whenValid_thenDoNothing() {
        AuditionEvent auditionEvent = AuditionEvent.builder()
                .id(1L)
                .user(User.builder().build())
                .eventType(AuditionEventType.REGISTRATION)
                .message("User logged in")
                .build();

        auditionEventService.save(auditionEvent);

        Mockito.verify(auditionEventRepository, Mockito.times(1)).save(auditionEvent);
    }
}
