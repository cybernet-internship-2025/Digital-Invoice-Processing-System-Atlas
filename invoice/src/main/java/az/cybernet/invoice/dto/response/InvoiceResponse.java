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
public class InvoiceResponse {
    Integer id;
    String series;
    Integer invoiceNumber;
    Integer senderId;
    Integer customerId;
    Status status;
    Double total;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String comment;
}