package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.client.UserClient;
import az.cybernet.invoice.dto.request.InvoiceFilterRequest;
import az.cybernet.invoice.dto.response.FilteredInvoiceResp;
import az.cybernet.invoice.dto.response.UserResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.ReturnTypeInvoice;
import az.cybernet.invoice.mapper.InvoiceViewMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class InvoiceViewService {
    private final UserClient userClient;
    private final InvoiceViewMapper invoiceViewMapper;
    public InvoiceViewService(UserClient userClient, InvoiceViewMapper invoiceViewMapper) {
        this.userClient = userClient;
        this.invoiceViewMapper = invoiceViewMapper;
    }
    public List<FilteredInvoiceResp> getSentInvoicesByTaxId(String taxId, InvoiceFilterRequest invoiceFilterRequest) {
        UserResponse userResponse = userClient.getUserByTaxId(taxId);
        String series = null;
        Integer invoiceNumber = null;
        if (StringUtils.hasText(invoiceFilterRequest.getFullInvoiceNumber())) {
            series = invoiceFilterRequest.getFullInvoiceNumber().replaceAll("\\d", "");
            invoiceNumber = Integer.parseInt(invoiceFilterRequest.getFullInvoiceNumber().replaceAll("\\D", ""));
        }
        return invoiceViewMapper.getSentInvoicesById(userResponse.getId(), invoiceFilterRequest, series, invoiceNumber);
    }
    public List<FilteredInvoiceResp> getReceivedInvoicesByTaxId(String taxId, InvoiceFilterRequest invoiceFilterRequest) {
        UserResponse userResponse = userClient.getUserByTaxId(taxId);
        String series = null;
        Integer invoiceNumber = null;
        if (StringUtils.hasText(invoiceFilterRequest.getFullInvoiceNumber())) {
            series = invoiceFilterRequest.getFullInvoiceNumber().replaceAll("\\d", "");
            invoiceNumber = Integer.parseInt(invoiceFilterRequest.getFullInvoiceNumber().replaceAll("\\D", ""));
        }
        return invoiceViewMapper.getReceivedInvoicesById(userResponse.getId(), invoiceFilterRequest, series, invoiceNumber);
    }
    public List<Invoice> getAllDraftsByTaxId(String taxId) {
        UserResponse userResponse = userClient.getUserByTaxId(taxId);
        return invoiceViewMapper.getAllDraftsById(userResponse.getId());
    }
    public List<Invoice> getAllInvoicesByTaxId(String taxId) {
        UserResponse userResponse = userClient.getUserByTaxId(taxId);
        return invoiceViewMapper.getAllInvoicesById(userResponse.getId());
    }
}
