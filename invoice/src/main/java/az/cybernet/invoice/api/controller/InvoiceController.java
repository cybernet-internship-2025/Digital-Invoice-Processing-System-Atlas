package az.cybernet.invoice.api.controller;

import az.cybernet.invoice.api.service.InvoiceBatchOperationsService;
import az.cybernet.invoice.dto.request.InvoiceBatchStatusUpdateRequest;
import az.cybernet.invoice.dto.request.InvoiceCorrectionReq;
import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.api.service.InvoiceService;
import jakarta.validation.Valid;
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
    public ResponseEntity<InvoiceResponse> insertInvoice(@RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(service.insertInvoice(request));
    }

    @PatchMapping("/correction/{id}")
    public ResponseEntity<InvoiceResponse> sendBackForCorrection(@PathVariable ("id") UUID id
            , @RequestBody @Valid InvoiceCorrectionReq req) {
        return ok(service.sendBackForCorrection(id, req));
    }

    @PostMapping("/batch-operations")
    public ResponseEntity<Void> changeStatusInBatch(@RequestBody InvoiceBatchStatusUpdateRequest req) {
        batchService.changeStatusInBatch(req.getInvoiceIds(), req.getNewStatus());
        return ResponseEntity.ok().build();
    }
}
