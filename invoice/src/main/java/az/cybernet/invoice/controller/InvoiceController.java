package az.cybernet.invoice.controller;

import az.cybernet.invoice.dto.request.*;
import az.cybernet.invoice.dto.response.FilteredInvoiceResp;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.service.InvoiceBatchOperationsService;
import az.cybernet.invoice.dto.response.InvoiceDetailResponse;
import az.cybernet.invoice.dto.response.InvoiceResponse;
import az.cybernet.invoice.service.InvoiceService;
import az.cybernet.invoice.util.HtmltoPdfConverter;
import az.cybernet.invoice.util.ExcelFileExporter;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.http.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/v1/invoices")
@Validated
@RequiredArgsConstructor
public class InvoiceController {

    private final InvoiceService service;
    private final InvoiceBatchOperationsService batchService;
    private final ExcelFileExporter<Invoice> excelFileExporter;

    @PostMapping
    public ResponseEntity<InvoiceResponse> createInvoice(@RequestBody @Valid CreateInvoiceRequest request) {
        return new ResponseEntity<>(service.createInvoice(request), HttpStatus.CREATED);
    }

    @PatchMapping("/correction/{id}")
    public ResponseEntity<InvoiceResponse> sendBackForCorrection(@PathVariable("id") UUID id
            , @RequestBody @Valid InvoiceCorrectionReq req) {
        return ok(service.sendBackForCorrection(id, req));
    }

    @PostMapping("/statuses/batch-update")
    public ResponseEntity<Void> changeStatusInBatch(@RequestBody InvoiceBatchStatusUpdateRequest req) {
        batchService.changeStatusInBatch(req.getInvoiceIds(), req.getNewStatus());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/cancel/{id}")
    public ResponseEntity<InvoiceResponse> cancelInvoice(@PathVariable("id") UUID id) {
        return ok(service.cancelInvoice(id));
    }

    @PatchMapping("/update")
    public ResponseEntity<InvoiceResponse> updateInvoice(@RequestBody UpdateInvoiceRequest req) {
        return ok(service.updateInvoice(req));
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<InvoiceDetailResponse> getInvoiceById(@PathVariable("invoiceId") UUID invoiceId) {
        InvoiceDetailResponse invoiceDetails = service.getInvoiceDetails(invoiceId);
        return ResponseEntity.ok(invoiceDetails);
    }

    @GetMapping("/{id}/export-to-excel")
    public ResponseEntity<byte[]> exportInvoiceToExcel(
            @PathVariable ("id") UUID id,
            @RequestParam(value = "fileName", defaultValue = "Invoice") String fileName) {
        return excelFileExporter.buildExcelResponse(service.exportInvoice(id), fileName);
    }

    @PatchMapping("/approve/{id}")
    public ResponseEntity<InvoiceResponse> approveInvoice(@PathVariable("id") UUID id) {
        return ok(service.approveInvoice(id));
    }

    @GetMapping(value = "/{id}/html", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> getInvoiceHtml(@PathVariable("id") UUID id) {
        String html = service.generateInvoiceHtml(id);
        return ResponseEntity.ok(html);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> getInvoicePdf(@PathVariable("id") UUID id) {
        String html = service.generateInvoiceHtml(id);
        byte[] pdfBytes = HtmltoPdfConverter.generatePdfFromHtml(html);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.inline().filename("invoice.pdf").build());

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }

    @PostMapping("/filter")
    public ResponseEntity<List<FilteredInvoiceResp>> filterInvoices(
            @RequestBody @Valid InvoiceFilterRequest invoiceFilterRequest
    ) {
        List<FilteredInvoiceResp> result = service.filterInvoices(invoiceFilterRequest);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/restore/{id}")
    public ResponseEntity<InvoiceResponse> restoreInvoice(@PathVariable("id") UUID id) {
        return ok(service.restoreCanceledInvoice(id));
    }
}
