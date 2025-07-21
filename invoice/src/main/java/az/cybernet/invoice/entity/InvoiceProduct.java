package az.cybernet.invoice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvoiceProduct {
    Integer invoiceId;
    Integer productId;
    Integer quantity;
    boolean isActive;
}
