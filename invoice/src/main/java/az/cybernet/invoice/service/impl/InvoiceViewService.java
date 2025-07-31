package az.cybernet.invoice.service.impl;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.mapper.InvoiceViewMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InvoiceViewService {
    private final InvoiceViewMapper invoiceViewMapper;
    public InvoiceViewService(InvoiceViewMapper invoiceViewMapper) {
        this.invoiceViewMapper = invoiceViewMapper;
    }
    public List<Invoice> getSentInvoicesByTaxId(String taxId) {
        return invoiceViewMapper.getSentInvoicesByTaxId(taxId);
    }
    public List<Invoice> getReceivedInvoicesByTaxId(String taxId) {
        return invoiceViewMapper.getReceivedInvoicesByTaxId(taxId);
    }
    public List<Invoice> getAllDraftsByTaxId(String taxId) {
        return invoiceViewMapper.getAllDraftsByTaxId(taxId);
    }
    public List<Invoice> getAllInvoicesByTaxId(String taxId) {
        return invoiceViewMapper.getAllInvoicesByTaxId(taxId);
    }
}
