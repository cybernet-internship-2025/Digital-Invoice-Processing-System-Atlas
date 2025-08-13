package az.cybernet.invoice.dto.response;

import az.cybernet.invoice.enums.InvoiceType;
import az.cybernet.invoice.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FilteredInvoiceResp {
    String fullInvoiceNumber;
    UUID senderId;
    UUID customerId;
    Status status;
    Double total;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String comment;
    InvoiceType type;
}
