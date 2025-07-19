package az.cybernet.invoice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvoiceProduct {
    int invoiceId;
    int productId;
    int quantity;
    boolean isActive;
}
