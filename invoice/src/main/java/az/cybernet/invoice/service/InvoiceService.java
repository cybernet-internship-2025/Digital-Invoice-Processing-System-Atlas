package az.cybernet.invoice.service;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.request.InvoiceCorrectionReq;
import az.cybernet.invoice.dto.request.UpdateInvoiceRequest;
import az.cybernet.invoice.dto.response.FilteredInvoiceResp;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.enums.InvoiceType;
import az.cybernet.invoice.enums.Status;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface InvoiceService {

    InvoiceResponse createInvoice(CreateInvoiceRequest request);

    InvoiceResponse sendBackForCorrection(UUID id, @Valid InvoiceCorrectionReq req);

    String generateInvoiceNumber();

    InvoiceResponse cancelInvoice(UUID id);

    InvoiceResponse updateInvoice(UpdateInvoiceRequest request);

    InvoiceDetailResponse getInvoiceDetails(UUID id);

    InvoiceResponse approveInvoice(UUID id);

    ResponseEntity<byte[]> getInvoicePdf(UUID id);

    void cancelOldPendingInvoices();

    List<FilteredInvoiceResp> filterInvoices(Integer year, LocalDate fromDate
            , LocalDate toDate, Status status, String fullInvoiceNumber, InvoiceType type);
}
