package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.service.InvoiceOperationService;
import az.cybernet.invoice.dto.request.InvoiceOperationRequest;
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
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvoiceBatchOperationsServiceImpl implements InvoiceBatchOperationsService {

    private final InvoiceBatchOperationsMapper mapper;
    private final InvoiceOperationService invoiceOperationService;

    @Override
    @Transactional
    public void changeStatusInBatch(List<UUID> invoiceIds, Status newStatus) {
        if (isBatchIdsEmpty(invoiceIds)) {
            throw new IllegalInvoiceException("Batch is empty");
        }
        List<Invoice> invoices = mapper.findAllByIds(invoiceIds);

        if (!areAllStatusesSame(invoices)) {
            throw new IllegalInvoiceException("Not all invoices have the same status");
        }
        changePreviousStatus(invoices, newStatus);
    }

    private boolean areAllStatusesSame(List<Invoice> invoices) {
        if (isBatchEmpty(invoices)) {
            throw new IllegalInvoiceException("Batch is empty");
        }

        return invoices.stream()
                .map(Invoice::getStatus)
                .collect(Collectors.toSet())
                .size() == 1;
    }

    private boolean isBatchEmpty(List<Invoice> invoices) {
        return invoices == null || invoices.isEmpty();
    }

    private boolean isBatchIdsEmpty(List<UUID> ids) {
        return ids == null || ids.isEmpty();
    }

    private void changePreviousStatus(List<Invoice> invoices, Status newStatus) {
        Status currentStatus = invoices.getFirst().getStatus();

        if (currentStatus.canBeChangedTo(newStatus)) {
            updateStatus(invoices, newStatus);
        } else {
            throw new IllegalInvoiceException(currentStatus + " status cannot be changed to: " + newStatus);
        }
    }

    private void updateStatus(List<Invoice> invoices, Status newStatus) {
        List<UUID> ids = invoices.stream()
                .map(Invoice::getId)
                .toList();
        for (Invoice invoice : invoices) {
            invoice.setStatus(newStatus);
            invoice.setUpdatedAt(LocalDateTime.now());

            InvoiceOperationRequest req = new InvoiceOperationRequest();
            req.setId(UUID.randomUUID());
            req.setInvoiceId(invoice.getId());
            req.setStatus(newStatus);
            req.setTotal(invoice.getTotal());
            req.setComment(invoice.getComment());
            invoiceOperationService.insertInvoiceOperation(req);
        }
        mapper.updateStatusInBatch(ids, newStatus, LocalDateTime.now());
    }
}

