package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.CreateInvoiceRequest;
import az.cybernet.invoice.dto.request.InvoiceCorrectionReq;
import az.cybernet.invoice.dto.request.UpdateInvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.service.InvoiceService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody CreateInvoiceRequest request) {
        return new ResponseEntity<>(service.createInvoice(request), HttpStatus.CREATED);
    }

    @PatchMapping("/correction/{id}")
    public ResponseEntity<InvoiceResponse> sendBackForCorrection(@PathVariable ("id") UUID id
            , @RequestBody @Valid InvoiceCorrectionReq req) {
        return ok(service.sendBackForCorrection(id, req));
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<InvoiceResponse> cancelInvoice(@PathVariable("id") UUID id) {
        return ok(service.cancelInvoice(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<InvoiceResponse> updateInvoice(@RequestBody UpdateInvoiceRequest req) {
        return ok(service.updateInvoice(req));
    }
}
