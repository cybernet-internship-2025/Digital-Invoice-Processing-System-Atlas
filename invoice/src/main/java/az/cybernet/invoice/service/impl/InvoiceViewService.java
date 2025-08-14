package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.client.UserClient;
import az.cybernet.invoice.dto.response.UserResponse;
import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.ReturnTypeInvoice;
import az.cybernet.invoice.mapper.InvoiceViewMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceViewService {
    private final UserClient userClient;
    private final InvoiceViewMapper invoiceViewMapper;
    public InvoiceViewService(UserClient userClient, InvoiceViewMapper invoiceViewMapper) {
        this.userClient = userClient;
        this.invoiceViewMapper = invoiceViewMapper;
    }
    public List<Invoice> getSentInvoicesByTaxId(String taxId) {
        UserResponse userResponse = userClient.getUserByTaxId(taxId);
        return invoiceViewMapper.getSentInvoicesById(userResponse.getId());
    }
    public List<Invoice> getReceivedInvoicesByTaxId(String taxId) {
        UserResponse userResponse = userClient.getUserByTaxId(taxId);
        return invoiceViewMapper.getReceivedInvoicesById(userResponse.getId());
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
