package az.cybernet.invoice.api.service.impl;

import az.cybernet.invoice.api.service.InvoiceOperationService;
import az.cybernet.invoice.dto.request.InvoiceOperationRequest;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.enums.Status;
import az.cybernet.invoice.exceptions.IllegalInvoiceException;
import az.cybernet.invoice.mapper.InvoiceBatchOperationsMapper;
import az.cybernet.invoice.api.service.InvoiceBatchOperationsService;
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

    public boolean areAllStatusesSame(List<Invoice> invoices) {
        if (isBatchEmpty(invoices)) {
            throw new IllegalInvoiceException("Batch is empty");
        }

        return invoices.stream()
                .map(Invoice::getStatus)
                .collect(Collectors.toSet())
                .size() == 1;
    }

    public boolean isBatchEmpty(List<Invoice> invoices) {
        return invoices == null || invoices.isEmpty();
    }

    public boolean isBatchIdsEmpty(List<UUID> ids) {
        return ids == null || ids.isEmpty();
    }

    public void changePreviousStatus(List<Invoice> invoices, Status newStatus) {
        Status currentStatus = invoices.getFirst().getStatus();
        if (currentStatus == Status.DRAFT) {
            if (newStatus == Status.PENDING) {
                updateStatus(invoices, Status.PENDING);
                return;
            } else if (newStatus == Status.CLOSED) {
                updateStatus(invoices, Status.CLOSED);
                return;
            } else {
                throw new IllegalInvoiceException("DRAFT status can not be changed to: " + newStatus);
            }
        }

        if (currentStatus == Status.PENDING) {
            if (newStatus == Status.APPROVED) {
                updateStatus(invoices, Status.APPROVED);
                return;
            } else if (newStatus == Status.REJECTED) {
                updateStatus(invoices, Status.REJECTED);
                return;
            } else {
                throw new IllegalInvoiceException("PENDING status can not be changed to: " + newStatus);
            }
        }

        if (currentStatus == Status.CHANGES_REQUESTED) {
            if (newStatus == Status.PENDING) {
                updateStatus(invoices, Status.PENDING);
                return;
            } else {
                throw new IllegalInvoiceException("CHANGES_REQUESTED status can not be changed to: " + newStatus);
            }
        }
        throw new IllegalInvoiceException(currentStatus + " status can not be changed to: " + newStatus);
    }

    public void updateStatus(List<Invoice> invoices, Status newStatus) {
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

