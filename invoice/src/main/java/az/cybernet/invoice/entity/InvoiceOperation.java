package az.cybernet.invoice.entity;

import az.cybernet.invoice.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvoiceOperation {
    Integer id;
    Integer invoiceId;
    Status status;
    Double total;
    LocalDateTime timestamp;
    String comment;
}
