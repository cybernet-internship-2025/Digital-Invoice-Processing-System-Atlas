package az.cybernet.invoice.entity;
import az.cybernet.invoice.dto.response.ProductDetailResponse;
import az.cybernet.invoice.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class ReturnTypeInvoice {
    UUID id;
    UUID originalInvoiceId; // Reference to the original invoice being returned
}
