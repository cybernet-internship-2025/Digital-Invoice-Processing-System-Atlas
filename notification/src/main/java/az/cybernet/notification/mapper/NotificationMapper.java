package az.cybernet.notification.mapper;

import az.cybernet.notification.dto.event.NotificationCreatedEvent;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface NotificationMapper {
    void insert(NotificationCreatedEvent event);
}
