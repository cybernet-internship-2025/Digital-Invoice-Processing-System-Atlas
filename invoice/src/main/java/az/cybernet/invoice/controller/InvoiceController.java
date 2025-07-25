package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.InvoiceRequest;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.service.InvoiceService;
import az.cybernet.invoice.service.impl.InvoiceServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/invoices")
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService service;


    @PostMapping
    public ResponseEntity<InvoiceResponse> insertInvoice(@RequestBody InvoiceRequest request) {
        return ResponseEntity.ok(service.insertInvoice(request));
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDetailResponse> getInvoiceById(@PathVariable UUID invoiceId) {
        InvoiceDetailResponse invoiceDetails = service.getInvoiceDetails(invoiceId);
        return ResponseEntity.ok(invoiceDetails);
    }

}
