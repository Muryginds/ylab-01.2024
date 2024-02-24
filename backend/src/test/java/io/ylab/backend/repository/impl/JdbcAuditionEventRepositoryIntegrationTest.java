package io.ylab.backend.repository.impl;

import io.ylab.backend.CommonIntegrationContainerBasedTest;
import io.ylab.commons.entity.AuditionEvent;
import io.ylab.commons.entity.User;
import io.ylab.commons.enumerated.AuditionEventType;
import io.ylab.commons.enumerated.UserRole;
import io.ylab.backend.mapper.UserMapper;
import io.ylab.backend.repository.AuditionEventRepository;
import io.ylab.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Transactional
class JdbcAuditionEventRepositoryIntegrationTest extends CommonIntegrationContainerBasedTest {
    @Autowired
    private AuditionEventRepository auditionEventRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        createTestData();
    }

    @Test
    void testGetEventsByUserId_whenExists_thenReturnCorrectSetOfEvents() {
        var user = userRepository.findUserByName("user1");
        assertTrue(user.isPresent());
        var userId = user.get().id();
        var auditionEventModels = auditionEventRepository.getEventsByUserId(userId);

        assertEquals(2, auditionEventModels.size());
        assertTrue(auditionEventModels.stream().allMatch(event -> event.userId().equals(userId)));
    }

    @Test
    void testAddEvent_whenCorrectData_thenSavedEventModelCorrectly() {
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

        var savedEventOptional = auditionEventRepository.getEventsByUserId(user1.getId()).stream()
                .filter(event -> event.eventType().equals(eventType))
                .findFirst();

        assertTrue(savedEventOptional.isPresent());

        var savedEvent = savedEventOptional.get();
        assertEquals(user1.getId(), savedEvent.userId());
        assertEquals(eventType, savedEvent.eventType());
        assertEquals(message, savedEvent.message());
    }

    private void createTestData() {
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
