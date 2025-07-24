package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.InvoiceOperation;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface InvoiceOperationMapper {

    void insertInvoiceOperation(InvoiceOperation invoiceOperation);
}
