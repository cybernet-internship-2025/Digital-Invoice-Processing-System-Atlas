package az.cybernet.invoice.controller;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.service.impl.InvoiceViewService;
import io.swagger.v3.oas.annotations.Parameter;
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
    public ResponseEntity<List<Invoice>> getSentInvoicesByTaxId(@PathVariable("taxId") String taxId) {
        List<Invoice> invoices = invoiceViewService.getSentInvoicesByTaxId(taxId);
        return ResponseEntity.ok(invoices);
    }
    @GetMapping("/received/{taxId}")
    public ResponseEntity<List<Invoice>> getReceivedInvoicesByTaxId(@PathVariable("taxId") String taxId) {
        List<Invoice> invoices = invoiceViewService.getReceivedInvoicesByTaxId(taxId);
        return ResponseEntity.ok(invoices);
    }
    @GetMapping("/drafts/{taxId}")
    public ResponseEntity<List<Invoice>> getAllDraftsByTaxId(@PathVariable("taxId") String taxId) {
        List<Invoice> invoices = invoiceViewService.getAllDraftsByTaxId(taxId);
        return ResponseEntity.ok(invoices);
    }
    @GetMapping("/all/{taxId}")
    public ResponseEntity<List<Invoice>> getAllInvoicesByTaxId(@PathVariable("taxId") String taxId) {
        List<Invoice> invoices = invoiceViewService.getAllInvoicesByTaxId(taxId);
        return ResponseEntity.ok(invoices);
    }
}
