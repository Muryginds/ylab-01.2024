package io.ylab.service;

import io.ylab.dto.response.AuditionEventDto;
import io.ylab.entity.AuditionEvent;
import io.ylab.entity.User;
import io.ylab.enumerated.UserRole;
import io.ylab.exception.NoPermissionException;
import io.ylab.mapper.AuditionEventMapper;
import io.ylab.repository.AuditionEventRepository;
import io.ylab.utils.CurrentUserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuditionEventServiceTest {
    @Mock
    private AuditionEventRepository auditionEventRepository;

    @Mock
    private AuditionEventMapper auditionEventMapper;

    @InjectMocks
    private AuditionEventService auditionEventService;

    private MockedStatic<CurrentUserUtils> utilsMockedStatic;

    @BeforeEach
    void setUp() {
        utilsMockedStatic = Mockito.mockStatic(CurrentUserUtils.class);
    }

    @AfterEach
    void tearDown() {
        utilsMockedStatic.close();
    }

    @Test
    void testGetEvents_WithAdminRole_ReturnsEvents() {
        long userId = 123;
        var admin = User.builder().id(userId).name("admin").role(UserRole.ADMIN).build();
        when(CurrentUserUtils.getCurrentUser()).thenReturn(admin);
        var expectedEvents = new HashSet<AuditionEventDto>();
        when(auditionEventRepository.getEventsByUserId(userId)).thenReturn(Collections.emptySet());
        when(auditionEventMapper.toAuditionEventDTOs(any())).thenReturn(expectedEvents);

        Set<AuditionEventDto> actualEvents = auditionEventService.getEvents(userId);

        assertEquals(expectedEvents, actualEvents);
        verify(auditionEventRepository, times(1)).getEventsByUserId(userId);
        verify(auditionEventMapper, times(1)).toAuditionEventDTOs(any());
    }

    @Test
    void testGetEvents_WithNonAdminRole_ThrowsNoPermissionException() {
        long userId = 123;
        var user = User.builder().id(userId).name("user").role(UserRole.USER).build();
        when(CurrentUserUtils.getCurrentUser()).thenReturn(user);

        assertThrows(NoPermissionException.class, () -> auditionEventService.getEvents(userId));
        verify(auditionEventRepository, never()).getEventsByUserId(anyLong());
        verify(auditionEventMapper, never()).toAuditionEventDTOs(any());
    }
}
