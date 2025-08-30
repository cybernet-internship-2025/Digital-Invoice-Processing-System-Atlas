package az.cybernet.invoice.dto.event;

import az.cybernet.invoice.enums.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InvoiceCreatedEvent {
    private UUID userId;
    private EventStatus status;
}
