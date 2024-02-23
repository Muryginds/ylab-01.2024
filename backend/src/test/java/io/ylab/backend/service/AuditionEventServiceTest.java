package io.ylab.backend.service;

import io.ylab.backend.dto.response.AuditionEventDto;
import io.ylab.backend.entity.User;
import io.ylab.backend.enumerated.UserRole;
import io.ylab.backend.exception.NoPermissionException;
import io.ylab.backend.mapper.AuditionEventMapper;
import io.ylab.backend.repository.AuditionEventRepository;
import io.ylab.backend.utils.CurrentUserUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        var userId = 123L;
        var admin = User.builder().id(userId).name("admin").role(UserRole.ADMIN).build();
        when(CurrentUserUtils.getCurrentUser()).thenReturn(admin);
        var expectedEvents = new HashSet<AuditionEventDto>();
        when(auditionEventRepository.getEventsByUserId(userId)).thenReturn(Collections.emptySet());
        when(auditionEventMapper.toAuditionEventDTOs(ArgumentMatchers.any())).thenReturn(expectedEvents);

        var actualEvents = auditionEventService.getEvents(userId);

        assertEquals(expectedEvents, actualEvents);
        verify(auditionEventRepository, Mockito.times(1)).getEventsByUserId(userId);
        verify(auditionEventMapper, Mockito.times(1)).toAuditionEventDTOs(ArgumentMatchers.any());
    }

    @Test
    void testGetEvents_WithNonAdminRole_ThrowsNoPermissionException() {
        var userId = 123L;
        var user = User.builder().id(userId).name("user").role(UserRole.USER).build();
        when(CurrentUserUtils.getCurrentUser()).thenReturn(user);

        assertThrows(NoPermissionException.class, () -> auditionEventService.getEvents(userId));
        verify(auditionEventRepository, Mockito.never()).getEventsByUserId(ArgumentMatchers.anyLong());
        verify(auditionEventMapper, Mockito.never()).toAuditionEventDTOs(ArgumentMatchers.any());
    }
}
