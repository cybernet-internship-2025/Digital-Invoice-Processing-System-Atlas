package az.cybernet.usermanagement.service.impl;

import az.cybernet.usermanagement.dto.event.UserApprovedEvent;
import az.cybernet.usermanagement.service.NotificationProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaNotificationProducerServiceImpl implements NotificationProducerService {

    @Value("${spring.kafka.topics.notifications}")
    private String notificationTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendUserApprovedNotification(UserApprovedEvent event) {
        log.info("Sending UserApprovedEvent to Kafka topic '{}': {}", notificationTopic, event);
        try {
            kafkaTemplate.send(notificationTopic, event.getRecipientUserId().toString(), event);
        } catch (Exception e) {
            log.error("Failed to send UserApprovedEvent to Kafka topic '{}'", notificationTopic, e);
            // retry logic will be added in future improvements.
        }
    }
}