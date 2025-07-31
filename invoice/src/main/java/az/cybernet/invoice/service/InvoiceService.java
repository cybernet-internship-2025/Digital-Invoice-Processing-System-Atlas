package az.cybernet.invoice.service;

import az.cybernet.invoice.dto.request.InvoiceCorrectionReq;
import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface InvoiceService {

    InvoiceResponse insertInvoice(InvoiceRequest request);

    InvoiceResponse sendBackForCorrection(UUID id, @Valid InvoiceCorrectionReq req);


    String generateInvoiceNumber();

    InvoiceResponse cancelInvoice(UUID id);

}
