package az.cybernet.notification.entity;

import az.cybernet.notification.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Notification {
    UUID id;
    UUID userId;
    String message;
    Status status;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
