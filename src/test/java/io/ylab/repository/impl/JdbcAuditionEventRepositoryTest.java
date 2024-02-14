package io.ylab.repository.impl;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import io.ylab.CommonContainerBasedTest;
import io.ylab.entity.AuditionEvent;
import io.ylab.entity.User;
import io.ylab.enumerated.AuditionEventType;
import io.ylab.enumerated.UserRole;
import io.ylab.mapper.UserMapper;
import io.ylab.repository.AuditionEventRepository;
import io.ylab.repository.UserRepository;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcAuditionEventRepositoryTest extends CommonContainerBasedTest {
    private static AuditionEventRepository auditionEventRepository;
    private static UserRepository userRepository;

    @BeforeAll
    static void setUp() {
        userRepository = new JdbcUserRepository(dbConnectionFactory);
        auditionEventRepository = new JdbcAuditionEventRepository(dbConnectionFactory);
        createTestUsers();
        createTestAuditionEvents();
    }

    @Test
    void testGetEventsByUserId_whenExists_thenReturnCorrectSetOfEvents() {
        var userId = 2L;
        var auditionEventModels = auditionEventRepository.getEventsByUserId(userId);

        assertEquals(2, auditionEventModels.size());
        assertTrue(auditionEventModels.stream().allMatch(auditionEvent -> auditionEvent.userId().equals(userId)));
    }

    @Test
    void testAddEvent_whenCorrectData_thenSavedEventModelCorrectly() {
        var userId = 2L;
        var eventType = AuditionEventType.SESSION_START;
        var message = "Session started";
        var date = LocalDateTime.now();
        var user1 = UserMapper.MAPPER.toUser(userRepository.findUserByName("user1").orElseThrow());

        var newAuditionEvent = AuditionEvent.builder()
                .user(user1)
                .eventType(eventType)
                .message(message)
                .date(date)
                .build();

        auditionEventRepository.save(newAuditionEvent);

        var savedEventOptional = auditionEventRepository.getEventsByUserId(userId).stream()
                .filter(event -> event.eventType().equals(eventType))
                .findFirst();

        assertTrue(savedEventOptional.isPresent());

        var savedEvent = savedEventOptional.get();
        assertEquals(userId, savedEvent.userId());
        assertEquals(eventType, savedEvent.eventType());
        assertEquals(message, savedEvent.message());
    }

    private static void createTestUsers() {
        userRepository.save(User.builder().name("user1").password("password1").role(UserRole.USER).build());
    }

    private static void createTestAuditionEvents() {
        var user1 = UserMapper.MAPPER.toUser(userRepository.findUserByName("user1").orElseThrow());

        auditionEventRepository.save(AuditionEvent.builder()
                .user(user1)
                .eventType(AuditionEventType.SESSION_START)
                .message("Session started")
                .date(LocalDateTime.now())
                .build()
        );

        auditionEventRepository.save(AuditionEvent.builder()
                .user(user1)
                .eventType(AuditionEventType.SESSION_END)
                .message("Session ended")
                .date(LocalDateTime.now())
                .build()
        );
    }
}
