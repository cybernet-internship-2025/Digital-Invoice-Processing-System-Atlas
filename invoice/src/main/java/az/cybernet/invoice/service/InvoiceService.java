package az.cybernet.invoice.service;

import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;

import java.util.UUID;

public interface InvoiceService {

    InvoiceResponse insertInvoice(InvoiceRequest request);

    InvoiceDetailResponse getInvoiceDetails(UUID id);
}
