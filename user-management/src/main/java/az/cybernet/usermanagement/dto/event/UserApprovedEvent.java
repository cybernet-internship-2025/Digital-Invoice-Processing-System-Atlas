package az.cybernet.usermanagement.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserApprovedEvent {
    private UUID recipientUserId; // The ID of the user who should get the notification
    private String newUserId; // The 6-digit login ID
    private String newTaxId; // The VOEN
    private String eventType; // e.g., "USER_REGISTRATION_APPROVED"
}