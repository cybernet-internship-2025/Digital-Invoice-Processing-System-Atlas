package az.cybernet.notification.service;

import az.cybernet.notification.dto.event.NotificationCreatedEvent;

public interface NotificationConsumerService {
    void consumeMessage(NotificationCreatedEvent event);
}
