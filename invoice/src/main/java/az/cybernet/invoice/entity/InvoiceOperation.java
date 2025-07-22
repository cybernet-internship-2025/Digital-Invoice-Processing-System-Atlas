package az.cybernet.invoice.entity;

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
public class InvoiceOperation {
    UUID id;
    UUID invoiceId;
    Status status;
    Double total;
    LocalDateTime timestamp;
    String comment;
}
