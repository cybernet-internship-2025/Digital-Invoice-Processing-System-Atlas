package az.cybernet.invoice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvoiceOperation {
    int id;
    int invoiceId;
    String status;
    int total;
    LocalDateTime timestamp;
    String comment;
}
