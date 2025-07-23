package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.InvoiceProductRequest;
import az.cybernet.invoice.dto.response.InvoiceProductResponse;
import az.cybernet.invoice.service.InvoiceProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/invoiceProducts")
public class InvoiceProductController {

    private final InvoiceProductService service;

    public InvoiceProductController(InvoiceProductService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<InvoiceProductResponse> insertInvoiceProduct(@RequestBody InvoiceProductRequest request) {
        return ResponseEntity.ok(service.insertInvoiceProduct(request));
    }
}
