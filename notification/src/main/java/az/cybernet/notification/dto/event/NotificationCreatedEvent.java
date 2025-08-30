package az.cybernet.notification.dto.event;

import az.cybernet.notification.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationCreatedEvent {
    private UUID userId;
    private String message;
    private Status status;
}