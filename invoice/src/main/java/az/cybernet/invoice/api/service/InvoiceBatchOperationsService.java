package az.cybernet.invoice.api.service;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.enums.Status;

import java.util.List;
import java.util.UUID;

public interface InvoiceBatchOperationsService {
    void changeStatusInBatch(List<UUID> invoiceIds, Status newStatus);

    boolean areAllStatusesSame(List<Invoice> invoices);

    boolean isBatchEmpty(List<Invoice> invoices);

    boolean isBatchIdsEmpty(List<UUID> ids);

    void changePreviousStatus(List<Invoice> invoices, Status newStatus);

    void updateStatus(List<Invoice> invoices, Status newStatus);
}