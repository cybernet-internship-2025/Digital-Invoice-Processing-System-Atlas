package az.cybernet.invoice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class InvoiceProduct {
    UUID invoiceId;
    UUID productId;
    BigDecimal quantity;
    boolean active;
}
