package az.cybernet.invoice.entity;

import az.cybernet.invoice.enums.Status;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    Integer senderId;
    Integer customerId;
    Status status;
    Double total;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String comment;
}