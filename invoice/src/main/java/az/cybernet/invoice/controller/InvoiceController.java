package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.*;
import az.cybernet.invoice.dto.response.FilteredInvoiceResp;
import az.cybernet.invoice.enums.InvoiceType;
import az.cybernet.invoice.enums.Status;
import az.cybernet.invoice.service.InvoiceBatchOperationsService;
import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/invoices")
@Validated
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService service;
    private final InvoiceBatchOperationsService batchService;

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody @Valid CreateInvoiceRequest request) {
        return new ResponseEntity<>(service.createInvoice(request), HttpStatus.CREATED);
    }

    @PatchMapping("/correction/{id}")
    public ResponseEntity<InvoiceResponse> sendBackForCorrection(@PathVariable("id") UUID id
            , @RequestBody @Valid InvoiceCorrectionReq req) {
        return ok(service.sendBackForCorrection(id, req));
    }

    @PostMapping("/statuses/batch-update")
    public ResponseEntity<Void> changeStatusInBatch(@RequestBody InvoiceBatchStatusUpdateRequest req) {
        batchService.changeStatusInBatch(req.getInvoiceIds(), req.getNewStatus());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<InvoiceResponse> cancelInvoice(@PathVariable("id") UUID id) {
        return ok(service.cancelInvoice(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<InvoiceResponse> updateInvoice(@RequestBody UpdateInvoiceRequest req) {
        return ok(service.updateInvoice(req));
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDetailResponse> getInvoiceById(@PathVariable ("invoiceId") UUID invoiceId) {
        InvoiceDetailResponse invoiceDetails = service.getInvoiceDetails(invoiceId);
        return ResponseEntity.ok(invoiceDetails);
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<InvoiceResponse> approveInvoice(@PathVariable("id") UUID id) {
        return ok(service.approveInvoice(id));
    }
    @GetMapping("/{id}/pdf")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable("id") UUID id) {
        return service.getInvoicePdf(id);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<FilteredInvoiceResp>> filterInvoices(
            @RequestParam(name = "year", required = false) Integer year,
            @RequestParam(name = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(name = "toDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(name = "status", required = false) Status status,
            @RequestParam(name = "fullInvoiceNumber", required = false) String fullInvoiceNumber,
            @RequestParam(name = "type", required = false) InvoiceType type
    ) {
        List<FilteredInvoiceResp> result = service.filterInvoices(year, fromDate, toDate, status
                , fullInvoiceNumber, type);
        return ResponseEntity.ok(result);
    }
}
