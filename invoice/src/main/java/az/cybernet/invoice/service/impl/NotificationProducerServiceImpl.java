package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.service.NotificationProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotificationProducerServiceImpl implements NotificationProducerService {

    @Value("${spring.kafka.topics.notifications}")
    private String notificationTopic;

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Override
    public void sendInvoiceCreatedNotification(String userId) {
        log.info("Sending message to Kafka topic '{}': {}", notificationTopic, userId);
        try {
            kafkaTemplate.send(notificationTopic, userId);
        } catch (Exception e) {
            log.error("Failed to send message to Kafka topic '{}'", notificationTopic, e);
        }
    }
}
