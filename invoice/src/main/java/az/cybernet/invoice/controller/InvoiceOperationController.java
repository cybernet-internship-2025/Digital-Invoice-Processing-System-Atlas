package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.InvoiceOperationRequest;
import az.cybernet.invoice.dto.response.InvoiceOperationResponse;
import az.cybernet.invoice.service.InvoiceOperationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invoiceOperations")
public class InvoiceOperationController {

    private final InvoiceOperationService service;

    public InvoiceOperationController(InvoiceOperationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InvoiceOperationResponse> insertInvoiceOperation(@RequestBody InvoiceOperationRequest request) {
        return ResponseEntity.ok(service.insertInvoiceOperation(request));
    }
}
