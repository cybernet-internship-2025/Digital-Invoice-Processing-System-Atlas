package az.cybernet.invoice.dto.response;

import az.cybernet.invoice.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvoiceOperationResponse {
    Integer id;
    Integer invoiceId;
    Status status;
    Double total;
    LocalDateTime timestamp;
    String comment;
}
