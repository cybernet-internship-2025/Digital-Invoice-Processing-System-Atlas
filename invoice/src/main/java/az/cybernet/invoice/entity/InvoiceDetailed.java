package az.cybernet.invoice.entity;

import az.cybernet.invoice.dto.response.ProductDetailResponse;
import az.cybernet.invoice.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@EqualsAndHashCode (of = "id")
public class InvoiceDetailed {
    UUID id;
    String series;
    Integer invoiceNumber;
    UUID senderId;
    UUID customerId;
    Status status;
    BigDecimal total;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String comment;

    List<ProductDetailResponse> products;
}