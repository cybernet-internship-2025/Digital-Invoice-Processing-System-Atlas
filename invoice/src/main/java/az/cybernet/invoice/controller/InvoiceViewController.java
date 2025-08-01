package az.cybernet.invoice.controller;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.service.impl.InvoiceViewService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/invoices/view")
public class InvoiceViewController {
    private final InvoiceViewService invoiceViewService;
    public InvoiceViewController(InvoiceViewService invoiceViewService) {
        this.invoiceViewService = invoiceViewService;
    }

    @GetMapping("/sent/{taxId}")
    public ResponseEntity<List<Invoice>> getSentInvoicesByTaxId(@PathVariable String taxId) {
        return ResponseEntity.ok(invoiceViewService.getSentInvoicesByTaxId(taxId));
    }
    @GetMapping("/received/{taxId}")
    public ResponseEntity<List<Invoice>> getReceivedInvoicesByTaxId(@PathVariable String taxId) {
        return ResponseEntity.ok(invoiceViewService.getReceivedInvoicesByTaxId(taxId));
    }
    @GetMapping("/drafts/{taxId}")
    public ResponseEntity<List<Invoice>> getAllDraftsByTaxId(@PathVariable String taxId) {
        return ResponseEntity.ok(invoiceViewService.getAllDraftsByTaxId(taxId));
    }
    @GetMapping("/all/{taxId}")
    public ResponseEntity<List<Invoice>> getAllInvoicesByTaxId(@PathVariable String taxId) {
        return ResponseEntity.ok(invoiceViewService.getAllInvoicesByTaxId(taxId));
    }
}
