package az.cybernet.invoice.dto.request;

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
public class CreateInvoiceRequest{
    UUID id;
    String series;
    Integer invoiceNumber;
    UUID senderId;
    UUID customerId;
    Status status;
    String comment;
    List<ProductQuantityRequest> productQuantityRequests;
}

