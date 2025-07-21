package az.cybernet.invoice.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvoiceProductRequest {
    Integer invoiceId;
    Integer productId;
    Integer quantity;
    boolean isActive;
}
