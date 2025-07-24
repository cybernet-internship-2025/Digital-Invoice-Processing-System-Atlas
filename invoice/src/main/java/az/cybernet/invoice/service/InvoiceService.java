package az.cybernet.invoice.service;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;

public interface InvoiceService {

    InvoiceResponse createInvoice(CreateInvoiceRequest request);
}
