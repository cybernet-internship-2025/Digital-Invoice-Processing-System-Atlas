package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.InvoiceFilterRequest;
import az.cybernet.invoice.dto.response.FilteredInvoiceResp;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.service.impl.InvoiceViewService;
import az.cybernet.invoice.util.ExcelFileExporter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/invoices/view")
@Validated
@SecurityRequirement(name = "bearerAuth")
public class InvoiceViewController {
    private final InvoiceViewService invoiceViewService;
    private final ExcelFileExporter excelFileExporter;

    public InvoiceViewController(InvoiceViewService invoiceViewService, ExcelFileExporter excelFileExporter) {
        this.invoiceViewService = invoiceViewService;
        this.excelFileExporter = excelFileExporter;
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

    @PostMapping("/{taxId}/sent/export-to-excel")
    public ResponseEntity<byte[]> exportSentInvoiceToExcel(
            @PathVariable("taxId") String taxId,
            @RequestBody @Valid InvoiceFilterRequest invoiceFilterRequest,
            @RequestParam(value = "fileName", defaultValue = "Invoice") String fileName) {
        return excelFileExporter.buildExcelResponse(
                invoiceViewService.exportSentInvoice(taxId, invoiceFilterRequest),
                fileName
        );
    }

    @PostMapping("/{taxId}/received/export-to-excel")
    public ResponseEntity<byte[]> exportReceivedInvoiceToExcel(
            @PathVariable("taxId") String taxId,
            @RequestBody @Valid InvoiceFilterRequest invoiceFilterRequest,
            @RequestParam(value = "fileName", defaultValue = "Invoice") String fileName) {
        return excelFileExporter.buildExcelResponse(
                invoiceViewService.exportReceivedInvoice(taxId, invoiceFilterRequest),
                fileName
        );
    }
}
