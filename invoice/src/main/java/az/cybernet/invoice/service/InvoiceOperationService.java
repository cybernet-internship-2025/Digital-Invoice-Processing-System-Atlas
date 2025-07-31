package az.cybernet.invoice.service;

import az.cybernet.invoice.dto.request.InvoiceOperationRequest;
import az.cybernet.invoice.dto.response.InvoiceOperationResponse;

public interface InvoiceOperationService {

    InvoiceOperationResponse insertInvoiceOperation(InvoiceOperationRequest request);
}
