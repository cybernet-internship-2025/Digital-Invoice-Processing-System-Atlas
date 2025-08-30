package az.cybernet.notification.service;

import az.cybernet.notification.dto.event.NotificationCreatedEvent;

public interface NotificationService {
    void insert(NotificationCreatedEvent event);
}
