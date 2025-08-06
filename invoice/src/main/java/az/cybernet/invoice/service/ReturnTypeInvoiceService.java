package az.cybernet.invoice.service;


import az.cybernet.invoice.dto.request.CreateReturnTypeRequest;
import az.cybernet.invoice.entity.ReturnTypeInvoice;

public interface ReturnTypeInvoiceService {
    ReturnTypeInvoice createReturnTypeInvoice(CreateReturnTypeRequest returnTypeInvoice);
}
