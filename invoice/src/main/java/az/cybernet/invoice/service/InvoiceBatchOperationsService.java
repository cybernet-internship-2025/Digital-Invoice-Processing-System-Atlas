package az.cybernet.invoice.service;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.enums.Status;

import java.util.List;
import java.util.UUID;

public interface InvoiceBatchOperationsService {
    void changeStatusInBatch(List<UUID> invoiceIds, Status newStatus);

}