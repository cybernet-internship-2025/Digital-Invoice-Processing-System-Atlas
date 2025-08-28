package az.cybernet.invoice.entity;

import az.cybernet.invoice.enums.InvoiceType;
import az.cybernet.invoice.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Invoice {
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
    InvoiceType invoiceType;
}