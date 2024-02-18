package io.ylab.repository.impl;

import io.ylab.CommonIntegrationContainerBasedTest;
import io.ylab.entity.AuditionEvent;
import io.ylab.entity.User;
import io.ylab.enumerated.AuditionEventType;
import io.ylab.enumerated.UserRole;
import io.ylab.mapper.UserMapper;
import io.ylab.repository.AuditionEventRepository;
import io.ylab.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JdbcAuditionEventRepositoryIntegrationTest extends CommonIntegrationContainerBasedTest {
    private static AuditionEventRepository auditionEventRepository;
    private static UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @BeforeAll
    static void setUp() {
        auditionEventRepository = new JdbcAuditionEventRepository(getJdbcTemplate());
        userRepository = new JdbcUserRepository(getJdbcTemplate());
        createTestData();
    }

    @Test
    void testGetEventsByUserId_whenExists_thenReturnCorrectSetOfEvents() {
        var userId = 2L;
        var auditionEventModels = auditionEventRepository.getEventsByUserId(userId);

        assertEquals(2, auditionEventModels.size());
        assertTrue(auditionEventModels.stream().allMatch(event -> event.userId().equals(userId)));
    }

    @Test
    void testAddEvent_whenCorrectData_thenSavedEventModelCorrectly() {
        var userId = 2L;
        var eventType = AuditionEventType.SESSION_START;
        var message = "Session started";
        var date = LocalDateTime.now();
        var user1 = userMapper.toUser(userRepository.findUserByName("user1").orElseThrow());

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

    private static void createTestData() {
        var user1 = User.builder().name("user1").password("password1").role(UserRole.USER).build();

        userRepository.save(user1);

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
