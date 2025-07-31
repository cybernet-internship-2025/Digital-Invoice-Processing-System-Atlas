package az.cybernet.invoice.dto.request;

import az.cybernet.invoice.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceBatchStatusUpdateRequest {
    private List<UUID> invoiceIds;
    private Status newStatus;
}
