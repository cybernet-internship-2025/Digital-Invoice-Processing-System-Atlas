package az.cybernet.invoice.dto.request;

import az.cybernet.invoice.enums.Status;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PUBLIC)
@Builder
public class InvoiceFilterReq {
    Integer year;
    LocalDate fromDate;
    LocalDate toDate;
    Status status;
    @JsonIgnore
    String series;
    @JsonIgnore
    Integer invoiceNumber;
    String fullInvoiceNumber;

    public void setFullInvoiceNumber(String fullInvoiceNumber) {
        this.fullInvoiceNumber = fullInvoiceNumber;
        if (fullInvoiceNumber != null && fullInvoiceNumber.length() >= 4) {
            this.series = fullInvoiceNumber.replaceAll("\\d", "");
            String numPart = fullInvoiceNumber.replaceAll("\\D", "");
            try {
                this.invoiceNumber = Integer.parseInt(numPart);
            } catch (NumberFormatException e) {
                this.invoiceNumber = null;
            }
        }
    }
}
