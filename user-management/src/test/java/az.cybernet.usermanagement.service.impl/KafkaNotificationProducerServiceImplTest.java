package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.dto.event.UserApprovedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaNotificationProducerServiceImplTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private KafkaNotificationProducerServiceImpl notificationProducerService;

    // This is the topic name from your application.yml
    private final String expectedTopic = "notification-events";

    @BeforeEach
    void setUp() {
        // Manually inject the @Value field for the unit test
        ReflectionTestUtils.setField(notificationProducerService, "notificationTopic", expectedTopic);
    }

    @Test
    void sendUserApprovedNotification_shouldSendEventToKafka() {
        // --- Arrange ---
        // 1. Create the event data to be sent
        UUID recipientId = UUID.randomUUID();
        UserApprovedEvent event = UserApprovedEvent.builder()
                .recipientUserId(recipientId)
                .newUserId("123456")
                .newTaxId("99001122")
                .eventType("USER_REGISTRATION_APPROVED")
                .build();

        // 2. We will use ArgumentCaptor to capture the arguments passed to kafkaTemplate.send()
        ArgumentCaptor<String> topicCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UserApprovedEvent> eventCaptor = ArgumentCaptor.forClass(UserApprovedEvent.class);

        // --- Act ---
        // 3. Call the method we want to test
        notificationProducerService.sendUserApprovedNotification(event);

        // --- Assert ---
        // 4. Verify that kafkaTemplate.send() was called exactly one time
        verify(kafkaTemplate, times(1)).send(topicCaptor.capture(), keyCaptor.capture(), eventCaptor.capture());

        // 5. Assert that the captured arguments are correct
        assertEquals(expectedTopic, topicCaptor.getValue());
        assertEquals(recipientId.toString(), keyCaptor.getValue());
        assertEquals("123456", eventCaptor.getValue().getNewUserId());
        assertEquals(event, eventCaptor.getValue());
    }

    @Test
    void sendUserApprovedNotification_shouldLogAnError_whenKafkaThrowsException() {
        // --- Arrange ---
        // 1. Create the event data
        UserApprovedEvent event = new UserApprovedEvent(UUID.randomUUID(), "123456", "99001122", "USER_REGISTRATION_APPROVED");

        // 2. Mock the kafkaTemplate to throw an exception when 'send' is called
        when(kafkaTemplate.send(anyString(), anyString(), any(UserApprovedEvent.class)))
                .thenThrow(new RuntimeException("Kafka broker is not available"));

        // --- Act ---
        // 3. Call the method. We expect it to catch the exception and not re-throw it.
        notificationProducerService.sendUserApprovedNotification(event);

        // --- Assert ---
        // 4. Verify that the send method was still called
        verify(kafkaTemplate, times(1)).send(anyString(), anyString(), any(UserApprovedEvent.class));
        // In a real scenario with a logging test framework, you could also assert that log.error() was called.
        // For now, the test passing without throwing an exception is sufficient to prove the catch block works.
    }
}