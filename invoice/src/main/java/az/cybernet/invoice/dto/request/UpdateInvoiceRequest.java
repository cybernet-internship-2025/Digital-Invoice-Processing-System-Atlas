package az.cybernet.invoice.dto.request;

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
public class UpdateInvoiceRequest {
    UUID id;
    Status status;
    String comment;
    @Valid
    List<ProductQuantityRequest> productQuantityRequests;
}
