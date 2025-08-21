package az.cybernet.usermanagement.service;

import az.cybernet.usermanagement.dto.event.UserApprovedEvent;

public interface NotificationProducerService {

    // Sends a notification event when a user's registration is approved.
    void sendUserApprovedNotification(UserApprovedEvent event);
}