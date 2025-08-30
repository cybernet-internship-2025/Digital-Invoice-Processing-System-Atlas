package az.cybernet.notification.service.impl;

import az.cybernet.notification.dto.event.NotificationCreatedEvent;
import az.cybernet.notification.service.NotificationConsumerService;
import az.cybernet.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumerServiceImpl implements NotificationConsumerService {

    private final NotificationService notificationService;

    @Override
    @KafkaListener(topics = "invoice-notification", groupId = "notification-group")
    public void consumeMessage(NotificationCreatedEvent event) {
        log.info("Received message from Kafka topic '{}' ", event.getUserId());
        try {
            notificationService.insert(event);
        } catch (Exception e) {
            log.error("Failed to receive message from Kafka topic '{}' ", e.getMessage());
        }
    }
}
