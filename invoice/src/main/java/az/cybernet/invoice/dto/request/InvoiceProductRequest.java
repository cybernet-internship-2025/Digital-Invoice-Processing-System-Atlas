package az.cybernet.invoice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvoiceProductRequest {
    UUID invoiceId;
    UUID productId;
    Double quantity;
    boolean active;
}
