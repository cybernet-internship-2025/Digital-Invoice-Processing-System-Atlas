package az.cybernet.invoice.dto.request;

import az.cybernet.invoice.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CreateInvoiceRequest{
    UUID senderId;
    UUID customerId;
    String comment;
    List<ProductQuantityRequest> productQuantityRequests;
}

