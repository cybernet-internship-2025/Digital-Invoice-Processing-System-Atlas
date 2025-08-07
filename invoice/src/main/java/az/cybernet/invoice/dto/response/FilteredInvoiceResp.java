package az.cybernet.invoice.dto.response;

import az.cybernet.invoice.enums.InvoiceType;
import az.cybernet.invoice.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class FilteredInvoiceResp {
    @JsonIgnore
    String series;
    @JsonIgnore
    Integer invoiceNumber;
    String fullInvoiceNumber;
    UUID senderId;
    UUID customerId;
    Status status;
    Double total;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String comment;
    InvoiceType type;
}
