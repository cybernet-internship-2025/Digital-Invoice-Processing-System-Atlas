package az.cybernet.invoice.mapper;

import az.cybernet.invoice.entity.Invoice;
import az.cybernet.invoice.enums.Status;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface InvoiceMapper {
    void changeInvoiceStatus(List<Invoice> invoices, Status status);
}
