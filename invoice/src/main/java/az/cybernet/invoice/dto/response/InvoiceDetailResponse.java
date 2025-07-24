package az.cybernet.invoice.dto.response;

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
public class InvoiceDetailResponse {
    UUID id;
    String series;
    Integer invoiceNumber;
    Status status;
    Double total;
    String comment;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    UserResponse sender;
    UserResponse customer;
    List<ProductDetailResponse> products;
}