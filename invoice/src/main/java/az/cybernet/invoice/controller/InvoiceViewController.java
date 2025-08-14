package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.InvoiceFilterRequest;
import az.cybernet.invoice.dto.response.FilteredInvoiceResp;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.ReturnTypeInvoice;
import az.cybernet.invoice.service.impl.InvoiceViewService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/invoices/view")
@Validated
public class InvoiceViewController {
    private final InvoiceViewService invoiceViewService;

    public InvoiceViewController(InvoiceViewService invoiceViewService) {
        this.invoiceViewService = invoiceViewService;
    }

    @PostMapping("/sent/{taxId}")
    public ResponseEntity<List<FilteredInvoiceResp>> getSentInvoicesByTaxId(@PathVariable("taxId") String taxId
            , @RequestBody @Valid InvoiceFilterRequest invoiceFilterRequest) {
        List<FilteredInvoiceResp> invoices = invoiceViewService.getSentInvoicesByTaxId(taxId, invoiceFilterRequest);
        return ResponseEntity.ok(invoices);
    }
    @PostMapping("/received/{taxId}")
    public ResponseEntity<List<FilteredInvoiceResp>> getReceivedInvoicesByTaxId(@PathVariable("taxId") String taxId
            , @RequestBody @Valid InvoiceFilterRequest invoiceFilterRequest) {
        List<FilteredInvoiceResp> invoices = invoiceViewService.getReceivedInvoicesByTaxId(taxId, invoiceFilterRequest);
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
