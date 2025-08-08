package az.cybernet.invoice.service;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.request.InvoiceCorrectionReq;
import az.cybernet.invoice.dto.request.UpdateInvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import jakarta.validation.Valid;

import java.util.UUID;

public interface InvoiceService {

    InvoiceResponse createInvoice(CreateInvoiceRequest request);

    InvoiceResponse restoreCanceledInvoice(UUID id);

    InvoiceResponse sendBackForCorrection(UUID id, @Valid InvoiceCorrectionReq req);

    String generateInvoiceNumber();

    InvoiceResponse cancelInvoice(UUID id);

    InvoiceResponse updateInvoice(UpdateInvoiceRequest request);

    InvoiceDetailResponse getInvoiceDetails(UUID id);

    InvoiceResponse approveInvoice(UUID id);

     String generateInvoiceHtml(UUID invoiceId);

    void cancelExpiredPendingInvoices();

    byte[] exportInvoice(UUID id);
}
