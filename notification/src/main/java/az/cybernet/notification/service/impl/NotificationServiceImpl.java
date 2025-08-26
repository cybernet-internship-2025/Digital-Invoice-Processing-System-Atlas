package az.cybernet.notification.service.impl;

import az.cybernet.notification.mapper.NotificationMapper;
import az.cybernet.notification.service.NotificationService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationMapper notificationMapper;

    public NotificationServiceImpl(NotificationMapper notificationMapper) {
        this.notificationMapper = notificationMapper;
    }

    @Override
    public void insert(UUID userId) {
        notificationMapper.insert(userId);

    }
}
