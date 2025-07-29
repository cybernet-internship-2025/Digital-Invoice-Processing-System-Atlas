package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.ApprovedInvoiceRequest;
import az.cybernet.invoice.dto.request.InvoiceCorrectionReq;
import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/invoices")
@Validated
public class InvoiceController {

    private final InvoiceService service;

    public InvoiceController(InvoiceService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InvoiceResponse> insertInvoice(@RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(service.insertInvoice(request));
    }

    @PatchMapping("/correction/{id}")
    public ResponseEntity<InvoiceResponse> sendBackForCorrection(@PathVariable ("id") UUID id
            , @RequestBody @Valid InvoiceCorrectionReq req) {
        return ok(service.sendBackForCorrection(id, req));
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<InvoiceResponse> approveInvoice(
            @PathVariable("id")UUID id,
            @RequestBody ApprovedInvoiceRequest request) {
        return ResponseEntity.ok(service.approveInvoice(id, request));
    }
}
