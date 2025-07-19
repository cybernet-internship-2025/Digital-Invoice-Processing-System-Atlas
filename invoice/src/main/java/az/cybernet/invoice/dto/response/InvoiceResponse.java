package az.cybernet.invoice.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Invoice {
    int id;
    String series;
    int invoiceNumber;
    int senderId;
    int customerId;
    String status;
    int total;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String comment;
}