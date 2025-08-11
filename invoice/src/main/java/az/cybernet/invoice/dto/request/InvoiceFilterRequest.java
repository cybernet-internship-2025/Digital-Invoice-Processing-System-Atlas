package az.cybernet.invoice.dto.request;

import az.cybernet.invoice.enums.InvoiceType;
import az.cybernet.invoice.enums.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvoiceFilterRequest {

    Integer year;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate fromDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    LocalDate toDate;

    Status status;

    @Pattern(
            regexp = "^(INVD|INR)\\d{8}$",
            message = "Invalid invoice number format. Must start with INVD or INR followed by 8 digits"
    )
    String fullInvoiceNumber;

    InvoiceType type;

    @NotNull(message = "CustomerId cannot be null")
    UUID customerId;
}