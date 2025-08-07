package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.entity.InvoiceOperation;
import az.cybernet.invoice.enums.Status;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvoiceOperationMapper {

    void insertInvoiceOperation(InvoiceOperation invoiceOperation);

    Status previousStatusFor(Invoice invoice);
}
