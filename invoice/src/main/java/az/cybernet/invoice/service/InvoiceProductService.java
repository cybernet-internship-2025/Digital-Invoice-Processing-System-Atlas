package az.cybernet.invoice.service;

import az.cybernet.invoice.dto.request.InvoiceProductRequest;
import az.cybernet.invoice.dto.response.InvoiceProductResponse;

public interface InvoiceProductService {

    InvoiceProductResponse insertInvoiceProduct(InvoiceProductRequest request);
}
