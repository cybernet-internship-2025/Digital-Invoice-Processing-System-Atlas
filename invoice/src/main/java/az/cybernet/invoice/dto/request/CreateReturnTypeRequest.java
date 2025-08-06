package az.cybernet.invoice.dto.request;
import az.cybernet.invoice.dto.response.ProductDetailResponse;
import az.cybernet.invoice.enums.Status;
import jakarta.validation.Valid;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CreateReturnTypeRequest {
    UUID initialInvoiceId;
    String initialInvoiceSeries;
    UUID senderId;
    UUID customerId;
    String comment;
    @Valid
    List<ProductDetailResponse> productQuantityRequests;
}
