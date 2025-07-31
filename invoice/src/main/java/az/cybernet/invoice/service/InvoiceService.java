package az.cybernet.invoice.service;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.request.InvoiceCorrectionReq;
import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.request.UpdateInvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface InvoiceService {

    InvoiceResponse createInvoice(CreateInvoiceRequest request);

    InvoiceResponse sendBackForCorrection(UUID id, @Valid InvoiceCorrectionReq req);

    InvoiceResponse cancelInvoice(UUID id);

    InvoiceResponse updateInvoice(UpdateInvoiceRequest request);
}
