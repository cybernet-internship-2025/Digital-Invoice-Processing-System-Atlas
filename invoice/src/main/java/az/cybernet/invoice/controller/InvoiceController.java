package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.service.InvoiceBatchOperationsService;
import az.cybernet.invoice.dto.request.InvoiceBatchStatusUpdateRequest;
import az.cybernet.invoice.dto.request.InvoiceCorrectionReq;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody CreateInvoiceRequest request) {
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

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDetailResponse> getInvoiceById(@PathVariable ("invoiceId") UUID invoiceId) {
        InvoiceDetailResponse invoiceDetails = service.getInvoiceDetails(invoiceId);
        return ResponseEntity.ok(invoiceDetails);
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<InvoiceResponse> approveInvoice(@PathVariable("id") UUID id) {
        return ok(service.approveInvoice(id));
    }
}
