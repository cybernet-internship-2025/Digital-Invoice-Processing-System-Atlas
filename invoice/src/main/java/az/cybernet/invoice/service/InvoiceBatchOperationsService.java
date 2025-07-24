package az.cybernet.invoice.service;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.enums.Status;

import java.util.List;

public interface InvoiceBatchOperationsService {
    void changeStatusInBatch(List<Invoice> invoices, Status newStatus);
}
