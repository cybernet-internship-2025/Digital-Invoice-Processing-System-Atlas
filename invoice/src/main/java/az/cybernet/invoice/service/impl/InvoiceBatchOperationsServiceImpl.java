package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.enums.Status;
import az.cybernet.invoice.exceptions.IllegalInvoiceException;
import az.cybernet.invoice.mapper.InvoiceBatchOperationsMapper;
import az.cybernet.invoice.service.InvoiceBatchOperationsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class InvoiceBatchOperationsServiceImpl implements InvoiceBatchOperationsService {

    private final InvoiceBatchOperationsMapper mapper;

    @Override
    public void changeStatusInBatch(List<Invoice> invoices, Status newStatus) {

        if (invoices == null || invoices.isEmpty()) {
            throw new IllegalInvoiceException("No invoices chosen to change status");
        }
        Status currentStatus = invoices.getFirst().getStatus();

        boolean hasAllSameStatus = invoices.stream().allMatch(
                invoice -> invoice.getStatus() == currentStatus);

        if (!hasAllSameStatus) {
            throw new IllegalInvoiceException(
                    "Not all invoices have the same status"
            );
        }
        for (Invoice invoice : invoices) {
            invoice.setStatus(newStatus);
            invoice.setUpdatedAt(LocalDateTime.now());
        }
    }
}
