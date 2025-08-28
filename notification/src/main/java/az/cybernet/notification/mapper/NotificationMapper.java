package az.cybernet.notification.mapper;

import org.apache.ibatis.annotations.Mapper;

import java.util.UUID;

@Mapper
public interface NotificationMapper {
    void insert(UUID userId);
}
