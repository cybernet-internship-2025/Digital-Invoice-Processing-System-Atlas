package az.cybernet.invoice.dto.request;

import az.cybernet.invoice.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvoiceOperationRequest {
    UUID id;
    UUID invoiceId;
    Status status;
    Double total;
    LocalDateTime timestamp;
    String comment;
}
