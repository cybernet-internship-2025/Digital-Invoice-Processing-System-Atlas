package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.client.UserClient;
import az.cybernet.invoice.constant.InvoiceExportHeaders;
import az.cybernet.invoice.dto.request.InvoiceFilterRequest;
import az.cybernet.invoice.dto.response.FilteredInvoiceResp;
import az.cybernet.invoice.dto.response.UserResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.ReturnTypeInvoice;
import az.cybernet.invoice.mapper.InvoiceViewMapper;
import az.cybernet.invoice.util.ExcelFileExporter;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.UUID;

@Service
public class InvoiceViewService {
    private final UserClient userClient;
    private final InvoiceViewMapper invoiceViewMapper;
    private final ExcelFileExporter excelFileExporter;

    public InvoiceViewService(UserClient userClient, InvoiceViewMapper invoiceViewMapper, ExcelFileExporter excelFileExporter) {
        this.userClient = userClient;
        this.invoiceViewMapper = invoiceViewMapper;
        this.excelFileExporter = excelFileExporter;
    }

    public List<FilteredInvoiceResp> getSentInvoicesByTaxId(String taxId, InvoiceFilterRequest invoiceFilterRequest) {
        Parts parts = findInvoicesByTaxId(taxId, invoiceFilterRequest);
        return invoiceViewMapper.getSentInvoicesById(parts.id(), invoiceFilterRequest, parts.series(), parts.number());
    }
    public List<FilteredInvoiceResp> getReceivedInvoicesByTaxId(String taxId, InvoiceFilterRequest invoiceFilterRequest) {
        Parts parts = findInvoicesByTaxId(taxId, invoiceFilterRequest);
        return invoiceViewMapper.getReceivedInvoicesById(parts.id(), invoiceFilterRequest, parts.series(), parts.number());
    }
    public List<Invoice> getAllDraftsByTaxId(String taxId) {
        UserResponse userResponse = userClient.getUserByTaxId(taxId);
        return invoiceViewMapper.getAllDraftsById(userResponse.getId());
    }
    public List<Invoice> getAllInvoicesByTaxId(String taxId) {
        UserResponse userResponse = userClient.getUserByTaxId(taxId);
        return invoiceViewMapper.getAllInvoicesById(userResponse.getId());
    }

    public byte[] exportSentInvoice(String taxId, InvoiceFilterRequest invoiceFilterRequest) {
        String[] headers = InvoiceExportHeaders.HEADERS;
        Parts parts = findInvoicesByTaxId(taxId, invoiceFilterRequest);
        List<FilteredInvoiceResp> invoices = invoiceViewMapper.getSentInvoicesById(parts.id(), invoiceFilterRequest, parts.series(), parts.number());
        return excelFileExporter.createExcelForEntity(invoices, headers);
    }

    public byte[] exportReceivedInvoice(String taxId, InvoiceFilterRequest invoiceFilterRequest) {
        String[] headers = InvoiceExportHeaders.HEADERS;
        Parts parts = findInvoicesByTaxId(taxId, invoiceFilterRequest);
        List<FilteredInvoiceResp> invoices = invoiceViewMapper.getReceivedInvoicesById(parts.id(), invoiceFilterRequest, parts.series(), parts.number());
        return excelFileExporter.createExcelForEntity(invoices, headers);
    }

    private Parts findInvoicesByTaxId(String taxId, InvoiceFilterRequest invoiceFilterRequest) {
        UserResponse userResponse = userClient.getUserByTaxId(taxId);
        String series = null;
        Integer invoiceNumber = null;
        if (StringUtils.hasText(invoiceFilterRequest.getFullInvoiceNumber())) {
            series = invoiceFilterRequest.getFullInvoiceNumber().replaceAll("\\d", "");
            invoiceNumber = Integer.parseInt(invoiceFilterRequest.getFullInvoiceNumber().replaceAll("\\D", ""));
        }

        return new Parts(userResponse.getId(), series, invoiceNumber);
    }

    private record Parts(UUID id, String series, Integer number) {}
}
