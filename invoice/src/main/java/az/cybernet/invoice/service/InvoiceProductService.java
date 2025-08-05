package az.cybernet.invoice.service;

import az.cybernet.invoice.dto.request.InvoiceProductRequest;
import az.cybernet.invoice.dto.response.InvoiceProductResponse;

import java.util.UUID;

public interface InvoiceProductService {

    InvoiceProductResponse insertInvoiceProduct(InvoiceProductRequest request);

    int setInactive(UUID id);
}
